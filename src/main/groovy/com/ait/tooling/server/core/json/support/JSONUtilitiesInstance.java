/*
 * Copyright (c) 2014,2015,2016 Ahome' Innovation Technologies. All rights reserved.
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

package com.ait.tooling.server.core.json.support;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.ait.tooling.server.core.json.JSONArray;
import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.json.binder.JSONBinder;
import com.ait.tooling.server.core.json.parser.JSONParser;
import com.ait.tooling.server.core.json.parser.JSONParserException;
import com.ait.tooling.server.core.json.schema.JSONSchema;

public class JSONUtilitiesInstance implements IJSONUtilities
{
    private static final long                  serialVersionUID = 5967727052776606210L;

    private static final JSONUtilitiesInstance INSTANCE         = new JSONUtilitiesInstance();

    public static final JSONUtilitiesInstance getJSONUtilitiesInstance()
    {
        return INSTANCE;
    }

    protected JSONUtilitiesInstance()
    {
    }

    @Override
    public final JSONObject json()
    {
        return new JSONObject();
    }

    @Override
    public final JSONObject json(final Map<String, ?> map)
    {
        return new JSONObject(Objects.requireNonNull(map));
    }

    @Override
    public final JSONObject json(final String name, final Object value)
    {
        return new JSONObject(Objects.requireNonNull(name), value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final JSONObject json(final Collection<?> collection)
    {
        Objects.requireNonNull(collection);

        if (collection instanceof List)
        {
            return json((List<?>) collection);
        }
        else if (collection instanceof Map)
        {
            return json((Map<String, ?>) collection);
        }
        else
        {
            return json(new ArrayList<Object>(collection));
        }
    }

    @Override
    public final JSONObject json(final List<?> list)
    {
        return new JSONObject(Objects.requireNonNull(list));
    }

    @Override
    public final JSONSchema jsonSchema(final Map<String, ?> schema)
    {
        return JSONSchema.cast(json(Objects.requireNonNull(schema)));
    }

    @Override
    public final JSONObject jsonParse(final String string) throws JSONParserException
    {
        Objects.requireNonNull(string);

        final Object result = new JSONParser().parse(string);

        if ((null != result) && (result instanceof JSONObject))
        {
            return ((JSONObject) result);
        }
        return null;
    }

    @Override
    public final JSONObject jsonParse(final Reader reader) throws IOException, JSONParserException
    {
        Objects.requireNonNull(reader);

        final Object result = new JSONParser().parse(reader);

        if ((null != result) && (result instanceof JSONObject))
        {
            return ((JSONObject) result);
        }
        return null;
    }

    @Override
    public final JSONArray jarr()
    {
        return new JSONArray();
    }

    @Override
    public final JSONArray jarr(final JSONObject object)
    {
        Objects.requireNonNull(object);

        final JSONArray list = jarr();

        jarr().add(object);

        return list;
    }

    @Override
    public final JSONArray jarr(final List<?> list)
    {
        return new JSONArray(Objects.requireNonNull(list));
    }

    @Override
    public final JSONArray jarr(final Map<String, ?> map)
    {
        return jarr(new JSONObject(Objects.requireNonNull(map)));
    }

    @Override
    public final JSONArray jarr(final String name, final Object value)
    {
        return jarr(new JSONObject(Objects.requireNonNull(name), value));
    }

    @Override
    @SuppressWarnings("unchecked")
    public final JSONArray jarr(final Collection<?> collection)
    {
        Objects.requireNonNull(collection);

        if (collection instanceof List)
        {
            return jarr((List<?>) collection);
        }
        else if (collection instanceof Map)
        {
            return jarr((Map<String, ?>) collection);
        }
        else
        {
            return jarr(new ArrayList<Object>(collection));
        }
    }

    @Override
    public final JSONBinder binder()
    {
        return new JSONBinder();
    }
}
