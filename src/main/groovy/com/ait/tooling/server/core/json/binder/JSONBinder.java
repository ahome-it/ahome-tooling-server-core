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

package com.ait.tooling.server.core.json.binder;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.IOUtils;

import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.json.ParserException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONBinder extends AbstractDataBinder
{
    public JSONBinder()
    {
        super(new ObjectMapper());
    }

    public JSONBinder(final MapperFeature... features)
    {
        super(new ObjectMapper(), features);
    }

    public JSONBinder(final List<MapperFeature> features)
    {
        super(new ObjectMapper(), features);
    }

    @Override
    public void send(final File file, final Object object) throws ParserException
    {
        Objects.requireNonNull(object);

        try
        {
            if (object instanceof JSONObject)
            {
                final Writer writer = new FileWriter(file);

                ((JSONObject) object).writeJSONString(writer, isStrict());

                IOUtils.closeQuietly(writer);
            }
            else
            {
                super.send(file, object);
            }
        }
        catch (Exception e)
        {
            throw new ParserException(e);
        }
    }

    @Override
    public void send(final OutputStream stream, final Object object) throws ParserException
    {
        Objects.requireNonNull(object);

        try
        {
            if (object instanceof JSONObject)
            {
                ((JSONObject) object).writeJSONString(new OutputStreamWriter(stream), isStrict());
            }
            else
            {
                super.send(stream, object);
            }
        }
        catch (Exception e)
        {
            throw new ParserException(e);
        }
    }

    @Override
    public void send(final Writer writer, final Object object) throws ParserException
    {
        Objects.requireNonNull(object);

        try
        {
            if (object instanceof JSONObject)
            {
                ((JSONObject) object).writeJSONString(writer, isStrict());
            }
            else
            {
                super.send(writer, object);
            }
        }
        catch (Exception e)
        {
            throw new ParserException(e);
        }
    }

    @Override
    public BinderType getType()
    {
        return BinderType.JSON;
    }
}
