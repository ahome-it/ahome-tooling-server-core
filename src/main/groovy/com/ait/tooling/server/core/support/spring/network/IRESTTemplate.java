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

package com.ait.tooling.server.core.support.spring.network;

import java.util.Map;

import com.ait.tooling.common.api.java.util.IHTTPConstants;
import com.ait.tooling.server.core.json.JSONObject;

public interface IRESTTemplate extends IHTTPConstants
{
    public IRESTResponse get(String path);

    public IRESTResponse get(String path, IHTTPHeaders headers);

    public IRESTResponse get(String path, Map<String, String> params);

    public IRESTResponse get(String path, Map<String, String> params, IHTTPHeaders headers);

    public IRESTResponse put(String path, JSONObject body);

    public IRESTResponse put(String path, IHTTPHeaders headers);

    public IRESTResponse put(String path, JSONObject body, IHTTPHeaders headers);

    public IRESTResponse post(String path, JSONObject body);

    public IRESTResponse post(String path, IHTTPHeaders headers);

    public IRESTResponse post(String path, JSONObject body, IHTTPHeaders headers);

    public IRESTResponse patch(String path, JSONObject body);

    public IRESTResponse patch(String path, IHTTPHeaders headers);

    public IRESTResponse patch(String path, JSONObject body, IHTTPHeaders headers);

    public IRESTResponse delete(String path, JSONObject body);

    public IRESTResponse delete(String path, IHTTPHeaders headers);

    public IRESTResponse delete(String path, JSONObject body, IHTTPHeaders headers);
}
