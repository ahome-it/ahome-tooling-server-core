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

import java.io.Closeable;

import com.ait.tooling.server.core.json.JSONObject;

import wslite.soap.SOAPClient;

public interface ICoreNetworkProvider extends Closeable
{
    public IRESTResponse get(String path);

    public IRESTResponse get(String path, HTTPHeaders headers);

    public IRESTResponse get(String path, PathParameters params);

    public IRESTResponse get(String path, PathParameters params, HTTPHeaders headers);

    public IRESTResponse post(String path, JSONObject body);

    public IRESTResponse post(String path, JSONObject body, HTTPHeaders headers);

    public IRESTResponse post(String path, JSONObject body, PathParameters params);

    public IRESTResponse post(String path, JSONObject body, PathParameters params, HTTPHeaders headers);

    public SOAPClient soap(String path);
}
