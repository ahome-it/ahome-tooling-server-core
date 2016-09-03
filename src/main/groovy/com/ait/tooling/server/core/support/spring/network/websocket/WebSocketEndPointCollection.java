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
import java.io.IOException;
import java.util.LinkedHashMap;

import javax.websocket.Session;

import com.ait.tooling.server.core.json.JSONObject;

public class WebSocketEndPointCollection implements Closeable
{
    private LinkedHashMap<String, WebSocketServiceCollection> m_collection = new LinkedHashMap<String, WebSocketServiceCollection>();

    public WebSocketEndPointCollection()
    {
    }

    public void onMessage(final Session session, final String name, final String text, final boolean last) throws Exception
    {
        final WebSocketServiceCollection endp = m_collection.get(name);

        if (null != endp)
        {
            endp.onMessage(session, text, last);
        }
    }

    public boolean addEndPoint(final Session session, final String name, final IWebSocketService service)
    {
        WebSocketServiceCollection endp = m_collection.get(name);

        if (null != endp)
        {
            return endp.addEndPoint(session, service);
        }
        else
        {
            endp = new WebSocketServiceCollection();

            m_collection.put(name, endp);

            return endp.addEndPoint(session, service);
        }
    }

    public boolean removeEndPoint(final Session session, final String name)
    {
        final WebSocketServiceCollection endp = m_collection.get(name);

        if (null != endp)
        {
            return endp.removeEndPoint(session);
        }
        return false;
    }

    public void broadcast(final String name, final String text)
    {
        final WebSocketServiceCollection endp = m_collection.get(name);

        if (null != endp)
        {
            endp.broadcast(text);
        }
    }

    public void broadcast(final String name, final JSONObject json)
    {
        final WebSocketServiceCollection endp = m_collection.get(name);

        if (null != endp)
        {
            endp.broadcast(json);
        }
    }

    public void broadcast(final String name, final String text, final boolean last)
    {
        final WebSocketServiceCollection endp = m_collection.get(name);

        if (null != endp)
        {
            endp.broadcast(text, last);
        }
    }

    public IWebSocketServiceSession getWebSocketServiceSession(final String id)
    {
        for (WebSocketServiceCollection endp : m_collection.values())
        {
            final IWebSocketServiceSession sess = endp.getWebSocketServiceSession(id);

            if (null != sess)
            {
                return sess;
            }
        }
        return null;
    }

    @Override
    public void close() throws IOException
    {
        
    }
}
