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
import java.util.List;

import javax.websocket.Session;

import com.ait.tooling.server.core.json.JSONObject;

public interface IWebSocketServiceProvider extends Closeable
{
    public List<String> getWebSocketServiceNames();

    public List<IWebSocketService> getWebSocketServices();
    
    public IWebSocketService getWebSocketService(String name);

    public IWebSocketService getWebSocketService(String name, List<String> scopes);
    
    public IWebSocketServiceSession getWebSocketServiceSession(String id);

    public void broadcast(String name, String text);

    public void broadcast(String name, String text, boolean last);
    
    public void broadcast(String name, JSONObject json);
    
    public boolean addEndPoint(Session session, String name, IWebSocketService service);

    public boolean removeEndPoint(Session session, String name);

    public void onMessage(final Session session, final String name, final String text, final boolean last) throws Exception;
}