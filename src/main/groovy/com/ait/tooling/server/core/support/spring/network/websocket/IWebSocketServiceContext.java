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

import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.support.spring.IServerContext;

public interface IWebSocketServiceContext extends IWebSocketServiceSession
{
    public IServerContext getServerContext();

    public void broadcast(String name, String text);

    public void broadcast(String name, String text, boolean last);
    
    public void broadcast(String name, JSONObject json);
}