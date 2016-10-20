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

package com.ait.tooling.server.core.support.spring.network.websocket;

import java.io.Closeable;

import javax.websocket.Session;

import com.ait.tooling.common.api.types.IIdentified;
import com.ait.tooling.server.core.json.JSONArray;
import com.ait.tooling.server.core.json.JSONObject;

public interface IWebSocketServiceSession extends IIdentified, Closeable
{
    public boolean isOpen();

    public Session getSession();

    public String getPathParameter(String name);

    public boolean isStrict();

    public IWebSocketService getSerivce();

    public void reply(String text);

    public void reply(String text, boolean last);

    public void reply(JSONObject json);
    
    public void reply(JSONArray batch);
}