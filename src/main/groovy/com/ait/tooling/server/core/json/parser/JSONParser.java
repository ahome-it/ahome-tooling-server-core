/*
 * Copyright (c) 2017 Ahome' Innovation Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ait.tooling.server.core.json.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;

import com.ait.tooling.common.server.io.NoSyncStringReader;
import com.ait.tooling.server.core.json.JSONArray;
import com.ait.tooling.server.core.json.JSONObject;

/**
 * Parser for JSON text. Please note that JSONParser is NOT thread-safe.
 * 
 * @author FangYidong<fangyidong@yahoo.com.cn>
 */
public final class JSONParser
{
    public static final int S_INIT              = 0;

    public static final int S_IN_FINISHED_VALUE = 1;                       // string,number,boolean,null,object,array

    public static final int S_IN_OBJECT         = 2;

    public static final int S_IN_ARRAY          = 3;

    public static final int S_PASSED_PAIR_KEY   = 4;

    public static final int S_IN_PAIR_VALUE     = 5;

    public static final int S_END               = 6;

    public static final int S_IN_ERROR          = -1;

    private Yylex           m_lexer             = new Yylex((Reader) null);

    private Yytoken         m_token             = null;

    private int             m_phase             = S_INIT;

    public JSONParser()
    {
    }

    private int peekStatus(ArrayDeque<Integer> statusStack)
    {
        if (statusStack.size() == 0) return -1;
        return statusStack.getFirst();
    }

    /**
     *  Reset the parser to the initial state without resetting the underlying reader.
     *
     */
    private void reset()
    {
        m_token = null;

        m_phase = S_INIT;
    }

    /**
     * Reset the parser to the initial state with a new character reader.
     * 
     * @param in - The new character reader.
     * @throws IOException
     * @throws JSONParserException
     */
    private void reset(Reader in)
    {
        m_lexer.yyreset(in);

        reset();
    }

    /**
     * @return The position of the beginning of the current token.
     */
    private int getPosition()
    {
        return m_lexer.getPosition();
    }

    public JSONObject parse(final String in) throws JSONParserException
    {
        Object result = null;

        try
        {
            result = parse_(new NoSyncStringReader(in));
        }
        catch (IOException e)
        {
            throw new JSONParserException(e);
        }
        if (result instanceof JSONObject)
        {
            return ((JSONObject) result);
        }
        return null;
    }

    public JSONObject parse(final InputStream in) throws JSONParserException
    {
        Object result = null;

        try
        {
            result = parse_(new NoSyncProxyReader(in));
        }
        catch (IOException e)
        {
            throw new JSONParserException(e);
        }
        if (result instanceof JSONObject)
        {
            return ((JSONObject) result);
        }
        return null;
    }

    public JSONObject parse(final Reader in) throws JSONParserException
    {
        Object result = null;

        try
        {
            result = parse_(new BufferedReader(in));
        }
        catch (IOException e)
        {
            throw new JSONParserException(e);
        }
        if (result instanceof JSONObject)
        {
            return ((JSONObject) result);
        }
        return null;
    }

    /**
     * Parse JSON text into java object from the input source.
     *  
     * @param in
     * @return Instance of the following:
     *  org.json.simple.JSONObject,
     *  org.json.simple.JSONArray,
     *  java.lang.String,
     *  java.lang.Number,
     *  java.lang.Boolean,
     *  null
     * 
     * @throws IOException
     * @throws JSONParserException
     */
    @SuppressWarnings("unchecked")
    private Object parse_(final Reader in) throws IOException, JSONParserException
    {
        reset(in);

        final ArrayDeque<Integer> statusStack = new ArrayDeque<Integer>(2048);

        final ArrayDeque<Object> valuesStack = new ArrayDeque<Object>(2048);

        try
        {
            do
            {
                nextToken();

                switch (m_phase)
                {
                    case S_INIT:
                        switch (m_token.m_type)
                        {
                            case Yytoken.TYPE_VALUE:
                                m_phase = S_IN_FINISHED_VALUE;
                                statusStack.addFirst(m_phase);
                                valuesStack.addFirst(m_token.m_value);
                                break;
                            case Yytoken.TYPE_LEFT_BRACE:
                                m_phase = S_IN_OBJECT;
                                statusStack.addFirst(m_phase);
                                valuesStack.addFirst(new JSONObject());
                                break;
                            case Yytoken.TYPE_LEFT_SQUARE:
                                m_phase = S_IN_ARRAY;
                                statusStack.addFirst(m_phase);
                                valuesStack.addFirst(new JSONArray());
                                break;
                            default:
                                m_phase = S_IN_ERROR;
                        }// inner switch
                        break;

                    case S_IN_FINISHED_VALUE:
                        if (m_token.m_type == Yytoken.TYPE_EOF) return valuesStack.removeFirst();
                        else throw new JSONParserException(getPosition(), JSONParserException.ERROR_UNEXPECTED_TOKEN, m_token);

                    case S_IN_OBJECT:
                        switch (m_token.m_type)
                        {
                            case Yytoken.TYPE_COMMA:
                                break;
                            case Yytoken.TYPE_VALUE:
                                if (m_token.m_value instanceof String)
                                {
                                    valuesStack.addFirst(m_token.m_value);
                                    m_phase = S_PASSED_PAIR_KEY;
                                    statusStack.addFirst(m_phase);
                                }
                                else
                                {
                                    m_phase = S_IN_ERROR;
                                }
                                break;
                            case Yytoken.TYPE_RIGHT_BRACE:
                                if (valuesStack.size() > 1)
                                {
                                    statusStack.removeFirst();
                                    valuesStack.removeFirst();
                                    m_phase = peekStatus(statusStack);
                                }
                                else
                                {
                                    m_phase = S_IN_FINISHED_VALUE;
                                }
                                break;
                            default:
                                m_phase = S_IN_ERROR;
                                break;
                        }// inner switch
                        break;

                    case S_PASSED_PAIR_KEY:
                        switch (m_token.m_type)
                        {
                            case Yytoken.TYPE_COLON:
                                break;
                            case Yytoken.TYPE_VALUE:
                                statusStack.removeFirst();
                                String key = (String) valuesStack.removeFirst();
                                Map<String, Object> parent = (Map<String, Object>) valuesStack.getFirst();
                                parent.put(key, m_token.m_value);
                                m_phase = peekStatus(statusStack);
                                break;
                            case Yytoken.TYPE_LEFT_SQUARE:
                                statusStack.removeFirst();
                                key = (String) valuesStack.removeFirst();
                                parent = (Map<String, Object>) valuesStack.getFirst();
                                List<Object> newArray = new JSONArray();
                                parent.put(key, newArray);
                                m_phase = S_IN_ARRAY;
                                statusStack.addFirst(m_phase);
                                valuesStack.addFirst(newArray);
                                break;
                            case Yytoken.TYPE_LEFT_BRACE:
                                statusStack.removeFirst();
                                key = (String) valuesStack.removeFirst();
                                parent = (Map<String, Object>) valuesStack.getFirst();
                                Map<String, Object> newObject = new JSONObject();
                                parent.put(key, newObject);
                                m_phase = S_IN_OBJECT;
                                statusStack.addFirst(m_phase);
                                valuesStack.addFirst(newObject);
                                break;
                            default:
                                m_phase = S_IN_ERROR;
                        }
                        break;

                    case S_IN_ARRAY:
                        switch (m_token.m_type)
                        {
                            case Yytoken.TYPE_COMMA:
                                break;
                            case Yytoken.TYPE_VALUE:
                                List<Object> val = (List<Object>) valuesStack.getFirst();
                                val.add(m_token.m_value);
                                break;
                            case Yytoken.TYPE_RIGHT_SQUARE:
                                if (valuesStack.size() > 1)
                                {
                                    statusStack.removeFirst();
                                    valuesStack.removeFirst();
                                    m_phase = peekStatus(statusStack);
                                }
                                else
                                {
                                    m_phase = S_IN_FINISHED_VALUE;
                                }
                                break;
                            case Yytoken.TYPE_LEFT_BRACE:
                                val = (List<Object>) valuesStack.getFirst();
                                Map<String, Object> newObject = new JSONObject();
                                val.add(newObject);
                                m_phase = S_IN_OBJECT;
                                statusStack.addFirst(m_phase);
                                valuesStack.addFirst(newObject);
                                break;
                            case Yytoken.TYPE_LEFT_SQUARE:
                                val = (List<Object>) valuesStack.getFirst();
                                List<Object> newArray = new JSONArray();
                                val.add(newArray);
                                m_phase = S_IN_ARRAY;
                                statusStack.addFirst(m_phase);
                                valuesStack.addFirst(newArray);
                                break;
                            default:
                                m_phase = S_IN_ERROR;
                        }// inner switch
                        break;
                    case S_IN_ERROR:
                        throw new JSONParserException(getPosition(), JSONParserException.ERROR_UNEXPECTED_TOKEN, m_token);
                }// switch
                if (m_phase == S_IN_ERROR)
                {
                    throw new JSONParserException(getPosition(), JSONParserException.ERROR_UNEXPECTED_TOKEN, m_token);
                }
            }
            while (m_token.m_type != Yytoken.TYPE_EOF);
        }
        catch (IOException ie)
        {
            throw ie;
        }
        throw new JSONParserException(getPosition(), JSONParserException.ERROR_UNEXPECTED_TOKEN, m_token);
    }

    private void nextToken() throws JSONParserException, IOException
    {
        m_token = m_lexer.yylex();

        if (null == m_token)
        {
            m_token = new Yytoken(Yytoken.TYPE_EOF, null);
        }
    }
}
