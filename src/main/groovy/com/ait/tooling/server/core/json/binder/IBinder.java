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
import java.util.List;

import org.springframework.core.io.Resource;

import com.ait.tooling.server.core.json.JSONObject;
import com.fasterxml.jackson.databind.MapperFeature;

public interface IBinder
{
    public <T> T bind(File file, Class<T> claz);

    public <T> T bind(InputStream stream, Class<T> claz);

    public <T> T bind(Reader reader, Class<T> claz);

    public <T> T bind(Resource resource, Class<T> claz);

    public <T> T bind(String text, Class<T> claz);

    public <T> T bind(URL url, Class<T> claz);
    
    public <T> T bind(JSONObject json, Class<T> claz);

    public JSONObject bindJSON(File file);

    public JSONObject bindJSON(InputStream stream);

    public JSONObject bindJSON(Reader reader);

    public JSONObject bindJSON(Resource resource);

    public JSONObject bindJSON(String text);

    public JSONObject bindJSON(URL url);

    public IBinder configure(MapperFeature feature, boolean state);

    public IBinder disable(List<MapperFeature> features);

    public IBinder disable(MapperFeature... features);

    public IBinder enable(List<MapperFeature> features);

    public IBinder enable(MapperFeature... features);

    public boolean isEnabled(MapperFeature feature);

    public boolean isStrict();

    public void send(File file, Object object);

    public void send(OutputStream stream, Object object);

    public void send(Writer writer, Object object);

    public IBinder setStrict(boolean strict);

    public String toString(Object object);

    public BinderType getType();
    
    public JSONObject toJSONObject(Object object);
    
    public String toJSONString(Object object);
    
    public boolean canSerializeType(Class<?> type);
    
    public boolean canSerializeObject(Object object);
}