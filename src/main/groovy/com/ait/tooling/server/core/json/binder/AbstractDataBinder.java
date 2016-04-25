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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;

import com.ait.tooling.common.server.io.NoSyncStringBuilderWriter;
import com.ait.tooling.server.core.io.NoCloseProxyReader;
import com.ait.tooling.server.core.io.NoSyncOrCloseBufferedWriter;
import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.json.parser.JSONParser;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractDataBinder implements IBinder
{
    private final Logger m_logger = Logger.getLogger(getClass());

    private ObjectMapper m_mapper;

    private boolean      m_strict = false;

    @SuppressWarnings("unchecked")
    protected final static JSONObject MAKE(Map<?, ?> make)
    {
        return new JSONObject((Map<String, Object>) make);
    }

    protected AbstractDataBinder(final ObjectMapper mapper)
    {
        m_mapper = mapper;
    }

    protected AbstractDataBinder(final ObjectMapper mapper, final MapperFeature... features)
    {
        this(mapper);

        enable(features);
    }

    protected AbstractDataBinder(final ObjectMapper mapper, final List<MapperFeature> features)
    {
        this(mapper);

        enable(features);
    }

    @Override
    public IBinder setStrict(final boolean strict)
    {
        m_strict = strict;

        return this;
    }

    @Override
    public boolean isStrict()
    {
        return m_strict;
    }

    @Override
    public IBinder configure(final MapperFeature feature, final boolean state)
    {
        m_mapper = m_mapper.configure(feature, state);

        return this;
    }

    @Override
    public IBinder enable(final MapperFeature... features)
    {
        m_mapper = m_mapper.enable(features);

        return this;
    }

    @Override
    public IBinder enable(final List<MapperFeature> features)
    {
        for (MapperFeature feature : features)
        {
            m_mapper = m_mapper.enable(feature);
        }
        return this;
    }

    @Override
    public IBinder disable(final MapperFeature... features)
    {
        m_mapper = m_mapper.disable(features);

        return this;
    }

    @Override
    public IBinder disable(final List<MapperFeature> features)
    {
        for (MapperFeature feature : features)
        {
            m_mapper = m_mapper.disable(feature);
        }
        return this;
    }

    @Override
    public boolean isEnabled(final MapperFeature feature)
    {
        return m_mapper.isEnabled(feature);
    }

    @Override
    public <T> T bind(final File file, final Class<T> claz)
    {
        try
        {
            return m_mapper.readValue(file, claz);
        }
        catch (Exception e)
        {
            logger().error("bind(" + claz.getName() + ")", e);
        }
        return null;
    }

    @Override
    public <T> T bind(final InputStream stream, final Class<T> claz)
    {
        try
        {
            return m_mapper.readValue(new NoCloseProxyReader(stream), claz);
        }
        catch (Exception e)
        {
            logger().error("bind(" + claz.getName() + ")", e);
        }
        return null;
    }

    @Override
    public <T> T bind(final Reader reader, final Class<T> claz)
    {
        try
        {
            return m_mapper.readValue(new NoCloseProxyReader(reader), claz);
        }
        catch (Exception e)
        {
            logger().error("bind(" + claz.getName() + ")", e);
        }
        return null;
    }

    @Override
    public <T> T bind(final Resource resource, final Class<T> claz)
    {
        try
        {
            return m_mapper.readValue(resource.getInputStream(), claz);
        }
        catch (Exception e)
        {
            logger().error("bind(" + claz.getName() + ")", e);
        }
        return null;
    }

    @Override
    public <T> T bind(final URL url, final Class<T> claz)
    {
        try
        {
            return m_mapper.readValue(url, claz);
        }
        catch (Exception e)
        {
            logger().error("bind(" + claz.getName() + ")", e);
        }
        return null;
    }

    @Override
    public <T> T bind(final String text, final Class<T> claz)
    {
        try
        {
            return m_mapper.readValue(text, claz);
        }
        catch (Exception e)
        {
            logger().error("bind(" + claz.getName() + ")", e);
        }
        return null;
    }

    @Override
    public <T> T bind(final JSONObject json, final Class<T> claz)
    {
        return bind(json.toJSONString(isStrict()), claz);
    }

    @Override
    public JSONObject bindJSON(final File file)
    {
        try
        {
            return MAKE(bind(file, LinkedHashMap.class));
        }
        catch (Exception e)
        {
            logger().error("bindJSON()", e);
        }
        return null;
    }

    @Override
    public JSONObject bindJSON(final InputStream stream)
    {
        try
        {
            return MAKE(bind(stream, LinkedHashMap.class));
        }
        catch (Exception e)
        {
            logger().error("bindJSON()", e);
        }
        return null;
    }

    @Override
    public JSONObject bindJSON(final Reader reader)
    {
        try
        {
            return MAKE(bind(reader, LinkedHashMap.class));
        }
        catch (Exception e)
        {
            logger().error("bindJSON()", e);
        }
        return null;
    }

    @Override
    public JSONObject bindJSON(final Resource resource)
    {
        try
        {
            return MAKE(bind(resource, LinkedHashMap.class));
        }
        catch (Exception e)
        {
            logger().error("bindJSON()", e);
        }
        return null;
    }

    @Override
    public JSONObject bindJSON(final URL url)
    {
        try
        {
            return MAKE(bind(url, LinkedHashMap.class));
        }
        catch (Exception e)
        {
            logger().error("bindJSON()", e);
        }
        return null;
    }

    @Override
    public JSONObject bindJSON(final String text)
    {
        try
        {
            return MAKE(bind(text, LinkedHashMap.class));
        }
        catch (Exception e)
        {
            logger().error("bindJSON()", e);
        }
        return null;
    }

    @Override
    public void send(final File file, final Object object)
    {
        Objects.requireNonNull(object);

        try
        {
            m_mapper.writeValue(file, object);
        }
        catch (Exception e)
        {
            logger().error("send()", e);
        }
    }

    @Override
    public void send(final OutputStream stream, final Object object)
    {
        Objects.requireNonNull(object);

        try
        {
            m_mapper.writeValue(new NoSyncOrCloseBufferedWriter(stream), object);
        }
        catch (Exception e)
        {
            logger().error("send()", e);
        }
    }

    @Override
    public void send(final Writer writer, final Object object)
    {
        Objects.requireNonNull(object);

        try
        {
            m_mapper.writeValue(new NoSyncOrCloseBufferedWriter(writer), object);
        }
        catch (Exception e)
        {
            logger().error("send()", e);
        }
    }

    @Override
    public String toString(final Object object)
    {
        Objects.requireNonNull(object);

        try
        {
            return m_mapper.writeValueAsString(object);
        }
        catch (Exception e)
        {
            logger().error("toString()", e);
        }
        return null;
    }

    @Override
    public JSONObject toJSONObject(final Object object)
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
                final NoSyncStringBuilderWriter writer = new NoSyncStringBuilderWriter();

                getMapperForJSON().writeValue(writer, object);

                final JSONParser parser = new JSONParser();

                final Object result = parser.parse(writer.toString());

                if (null == result)
                {
                    logger().error("toJSONObject() JSONObject null");
                }
                else if (result instanceof JSONObject)
                {
                    return ((JSONObject) result);
                }
                else
                {
                    logger().error("toJSONObject() not JSONObject " + object.getClass().getName());
                }
            }
        }
        catch (Exception e)
        {
            logger().error("toJSONObject()", e);
        }
        return null;
    }

    @Override
    public String toJSONString(final Object object)
    {
        final JSONObject json = toJSONObject(object);

        if (null != json)
        {
            return json.toJSONString(isStrict());
        }
        return null;
    }

    @Override
    public boolean canSerializeType(final Class<?> type)
    {
        return m_mapper.canSerialize(Objects.requireNonNull(type));
    }

    @Override
    public boolean canSerializeObject(final Object object)
    {
        if (null != object)
        {
            return canSerializeType(object.getClass());
        }
        return false;
    }

    protected final Logger logger()
    {
        return m_logger;
    }

    protected ObjectMapper getMapperForJSON()
    {
        return new ObjectMapper().setConfig(m_mapper.getDeserializationConfig());
    }
}
