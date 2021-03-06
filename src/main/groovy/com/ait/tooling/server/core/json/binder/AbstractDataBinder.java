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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.core.io.Resource;

import com.ait.tooling.server.core.io.NoCloseProxyInputStream;
import com.ait.tooling.server.core.io.NoCloseProxyOutputStream;
import com.ait.tooling.server.core.io.NoCloseProxyReader;
import com.ait.tooling.server.core.io.NoCloseProxyWriter;
import com.ait.tooling.server.core.io.NoSyncStringBuilderWriter;
import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.json.ParserException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractDataBinder implements IBinder
{
    private ObjectMapper m_mapper;

    private boolean      m_strict = false;

    @SuppressWarnings("unchecked")
    protected final static JSONObject MAKE(final Object make)
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
    public <T> T bind(final File file, final Class<T> claz) throws ParserException
    {
        try
        {
            return m_mapper.readValue(file, claz);
        }
        catch (Exception e)
        {
            throw new ParserException(e);
        }
    }

    @Override
    public <T> T bind(final InputStream stream, final Class<T> claz) throws ParserException
    {
        try
        {
            return m_mapper.readValue(new NoCloseProxyInputStream(stream), claz);
        }
        catch (Exception e)
        {
            throw new ParserException(e);
        }
    }

    @Override
    public <T> T bind(final Reader reader, final Class<T> claz) throws ParserException
    {
        try
        {
            return m_mapper.readValue(new NoCloseProxyReader(reader), claz);
        }
        catch (Exception e)
        {
            throw new ParserException(e);
        }
    }

    @Override
    public <T> T bind(final Resource resource, final Class<T> claz) throws ParserException
    {
        try
        {
            return m_mapper.readValue(resource.getInputStream(), claz);
        }
        catch (Exception e)
        {
            throw new ParserException(e);
        }
    }

    @Override
    public <T> T bind(final URL url, final Class<T> claz) throws ParserException
    {
        try
        {
            return m_mapper.readValue(url, claz);
        }
        catch (Exception e)
        {
            throw new ParserException(e);
        }
    }

    @Override
    public <T> T bind(final String text, final Class<T> claz) throws ParserException
    {
        try
        {
            return m_mapper.readValue(text, claz);
        }
        catch (Exception e)
        {
            throw new ParserException(e);
        }
    }

    @Override
    public <T> T bind(final JSONObject json, final Class<T> claz) throws ParserException
    {
        return bind(json.toJSONString(isStrict()), claz);
    }

    @Override
    public JSONObject bindJSON(final File file) throws ParserException
    {
        return MAKE(bind(file, LinkedHashMap.class));
    }

    @Override
    public JSONObject bindJSON(final InputStream stream) throws ParserException
    {
        return MAKE(bind(stream, LinkedHashMap.class));
    }

    @Override
    public JSONObject bindJSON(final Reader reader) throws ParserException
    {
        return MAKE(bind(reader, LinkedHashMap.class));
    }

    @Override
    public JSONObject bindJSON(final Resource resource) throws ParserException
    {
        return MAKE(bind(resource, LinkedHashMap.class));
    }

    @Override
    public JSONObject bindJSON(final URL url) throws ParserException
    {
        return MAKE(bind(url, LinkedHashMap.class));
    }

    @Override
    public JSONObject bindJSON(final String text) throws ParserException
    {
        return MAKE(bind(text, LinkedHashMap.class));
    }

    @Override
    public void send(final File file, final Object object) throws ParserException
    {
        Objects.requireNonNull(object);

        try
        {
            m_mapper.writeValue(file, object);
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
            m_mapper.writeValue(new NoCloseProxyOutputStream(stream), object);
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
            m_mapper.writeValue(new NoCloseProxyWriter(writer), object);
        }
        catch (Exception e)
        {
            throw new ParserException(e);
        }
    }

    @Override
    public String toString(final Object object) throws ParserException
    {
        Objects.requireNonNull(object);

        try
        {
            return m_mapper.writeValueAsString(object);
        }
        catch (Exception e)
        {
            throw new ParserException(e);
        }
    }

    @Override
    public JSONObject toJSONObject(final Object object) throws ParserException
    {
        Objects.requireNonNull(object);

        try
        {
            if (object instanceof JSONObject)
            {
                return ((JSONObject) object);
            }
            else if (object instanceof Map)
            {
                return MAKE(object);
            }
            else if (object instanceof String)
            {
                return bindJSON(object.toString());
            }
            else
            {
                final NoSyncStringBuilderWriter writer = new NoSyncStringBuilderWriter();

                getMapperForJSON().writeValue(writer, object);

                return bindJSON(writer.toString());
            }
        }
        catch (Exception e)
        {
            throw new ParserException(e);
        }
    }

    @Override
    public String toJSONString(final Object object) throws ParserException
    {
        return toJSONObject(object).toJSONString(isStrict());
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

    protected ObjectMapper getMapperForJSON()
    {
        return new ObjectMapper();
    }
}
