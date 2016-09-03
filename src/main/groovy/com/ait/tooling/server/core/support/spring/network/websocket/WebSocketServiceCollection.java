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

public class WebSocketServiceCollection implements Closeable
{
    private final LinkedHashMap<String, WebSocketServiceContext> m_collection = new LinkedHashMap<String, WebSocketServiceContext>();

    public WebSocketServiceCollection()
    {
    }

    public void onMessage(final Session session, final String text, final boolean last) throws Exception
    {
        final WebSocketServiceContext entr = m_collection.get(session.getId());

        if (null != entr)
        {
            entr.onMessage(text, last);
        }
    }

    public boolean addEndPoint(final Session session, final IWebSocketService service)
    {
        final String id = session.getId();

        if (false == m_collection.containsKey(id))
        {
            m_collection.put(id, new WebSocketServiceContext(session, service));

            return true;
        }
        return false;
    }

    public boolean removeEndPoint(final Session session)
    {
        final String id = session.getId();

        if (m_collection.containsKey(id))
        {
            m_collection.remove(id);

            return true;
        }
        return false;
    }

    public void broadcast(final String text)
    {
        broadcast(text, true);
    }

    public void broadcast(final String text, final boolean last)
    {
        for (WebSocketServiceContext entry : m_collection.values())
        {
            entry.reply(text, last);
        }
    }

    public void broadcast(final JSONObject json)
    {
        String safe = null;

        String text = null;

        for (WebSocketServiceContext entry : m_collection.values())
        {
            if (entry.isStrict())
            {
                if (null == safe)
                {
                    safe = json.toJSONString(true);
                }
                entry.reply(safe);
            }
            else
            {
                if (null == text)
                {
                    text = json.toJSONString(false);
                }
                entry.reply(text);
            }
        }
    }

    public IWebSocketServiceSession getWebSocketServiceSession(final String id)
    {
        return m_collection.get(id);
    }

    @Override
    public void close() throws IOException
    {
        for (WebSocketServiceContext entry : m_collection.values())
        {
            try
            {
                entry.close();
            }
            catch (Exception e)
            {

            }
        }
    }
}
