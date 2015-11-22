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

package com.ait.tooling.server.core.json.binder;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.json.parser.JSONParser;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JSONBinder implements Serializable
{
    private static final long   serialVersionUID = 274974455359363594L;

    private static final Logger logger           = Logger.getLogger(JSONBinder.class);

    private ObjectMapper        m_mapper;

    public JSONBinder()
    {
        m_mapper = new ObjectMapper();
    }

    public JSONBinder(final MapperFeature... features)
    {
        m_mapper = new ObjectMapper().enable(features);
    }

    public JSONBinder(final List<MapperFeature> features)
    {
        m_mapper = new ObjectMapper();

        enable(features);
    }

    public JSONBinder configure(final MapperFeature feature, final boolean state)
    {
        m_mapper = m_mapper.configure(feature, state);

        return this;
    }

    public JSONBinder enable(final MapperFeature... features)
    {
        m_mapper = m_mapper.enable(features);

        return this;
    }

    public JSONBinder enable(final List<MapperFeature> features)
    {
        for (MapperFeature feature : features)
        {
            m_mapper = m_mapper.enable(feature);
        }
        return this;
    }

    public JSONBinder disable(final MapperFeature... features)
    {
        m_mapper = m_mapper.disable(features);

        return this;
    }

    public JSONBinder disable(final List<MapperFeature> features)
    {
        for (MapperFeature feature : features)
        {
            m_mapper = m_mapper.disable(feature);
        }
        return this;
    }

    public boolean isEnabled(final MapperFeature feature)
    {
        return m_mapper.isEnabled(feature);
    }

    public <T> T bind(final File file, final Class<T> claz)
    {
        try
        {
            return m_mapper.readValue(file, claz);
        }
        catch (Exception e)
        {
            logger.error("bind(" + claz.getName() + ")", e);
        }
        return null;
    }

    public <T> T bind(final InputStream stream, final Class<T> claz)
    {
        try
        {
            return m_mapper.readValue(stream, claz);
        }
        catch (Exception e)
        {
            logger.error("bind(" + claz.getName() + ")", e);
        }
        return null;
    }

    public <T> T bind(final Reader reader, final Class<T> claz)
    {
        try
        {
            return m_mapper.readValue(reader, claz);
        }
        catch (Exception e)
        {
            logger.error("bind(" + claz.getName() + ")", e);
        }
        return null;
    }

    public <T> T bind(final URL url, final Class<T> claz)
    {
        try
        {
            return m_mapper.readValue(url, claz);
        }
        catch (Exception e)
        {
            logger.error("bind(" + claz.getName() + ")", e);
        }
        return null;
    }

    public <T> T bind(final String text, final Class<T> claz)
    {
        try
        {
            return m_mapper.readValue(text, claz);
        }
        catch (Exception e)
        {
            logger.error("bind(" + claz.getName() + ")", e);
        }
        return null;
    }

    public <T> T bind(final JSONObject json, final Class<T> claz)
    {
        try
        {
            return m_mapper.readValue(json.toJSONString(false), claz);
        }
        catch (Exception e)
        {
            logger.error("bind(" + claz.getName() + ")", e);
        }
        return null;
    }

    public void send(final File file, final Object object)
    {
        Objects.requireNonNull(object);

        try
        {
            if (object instanceof JSONObject)
            {
                final Writer writer = new FileWriter(file);

                ((JSONObject) object).writeJSONString(writer);

                IOUtils.closeQuietly(writer);
            }
            else
            {
                m_mapper.writeValue(file, object);
            }
        }
        catch (Exception e)
        {
            logger.error("send()", e);
        }
    }

    public void send(final OutputStream stream, final Object object)
    {
        Objects.requireNonNull(object);

        try
        {
            if (object instanceof JSONObject)
            {
                ((JSONObject) object).writeJSONString(new OutputStreamWriter(stream));
            }
            else
            {
                m_mapper.writeValue(stream, object);
            }
        }
        catch (Exception e)
        {
            logger.error("send()", e);
        }
    }

    public void send(final Writer writer, final Object object)
    {
        Objects.requireNonNull(object);

        try
        {
            if (object instanceof JSONObject)
            {
                ((JSONObject) object).writeJSONString(writer);
            }
            else
            {
                m_mapper.writeValue(writer, object);
            }
        }
        catch (Exception e)
        {
            logger.error("send()", e);
        }
    }

    public String toJSONString(final Object object)
    {
        Objects.requireNonNull(object);

        try
        {
            if (object instanceof JSONObject)
            {
                return object.toString();
            }
            else
            {
                return m_mapper.writeValueAsString(object);
            }
        }
        catch (Exception e)
        {
            logger.error("toJSONString()", e);
        }
        return null;
    }

    public JSONObject toJSONObject(Object object)
    {
        Objects.requireNonNull(object);

        try
        {
            if (object instanceof JSONObject)
            {
                return ((JSONObject) object);
            }
            else
            {
                final JSONParser parser = new JSONParser();

                object = parser.parse(m_mapper.writeValueAsString(object));

                if (object instanceof JSONObject)
                {
                    return ((JSONObject) object);
                }
            }
        }
        catch (Exception e)
        {
            logger.error("toJSONObject()", e);
        }
        return null;
    }
}
