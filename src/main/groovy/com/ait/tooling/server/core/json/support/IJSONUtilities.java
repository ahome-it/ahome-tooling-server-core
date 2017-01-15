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

package com.ait.tooling.server.core.json.support;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.ait.tooling.server.core.json.JSONArray;
import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.json.binder.BinderType;
import com.ait.tooling.server.core.json.binder.IBinder;
import com.ait.tooling.server.core.json.schema.JSONSchema;

public interface IJSONUtilities
{
    public JSONObject json();

    public JSONObject json(Map<String, ?> map);

    public JSONObject json(String name, Object value);

    public JSONObject json(Collection<?> collection);

    public JSONObject json(List<?> list);

    public JSONSchema jsonSchema(Map<String, ?> schema);

    public JSONArray jarr();

    public JSONArray jarr(JSONObject object);

    public JSONArray jarr(List<?> list);

    public JSONArray jarr(Map<String, ?> map);

    public JSONArray jarr(String name, Object value);

    public JSONArray jarr(Collection<?> collection);

    public IBinder binder();

    public IBinder binder(BinderType type);
}