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

import java.util.LinkedHashMap;

import javax.websocket.Session;

import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.support.spring.IServerContext;
import com.ait.tooling.server.core.support.spring.ServerContextInstance;

public class WebSocketServiceCollection
{
    private final IServerContext                                      m_context;

    private final LinkedHashMap<String, WebSocketServiceContextEntry> m_collection = new LinkedHashMap<String, WebSocketServiceContextEntry>();

    public WebSocketServiceCollection()
    {
        m_context = ServerContextInstance.getServerContextInstance();
    }

    public boolean onMessage(final Session session, final String text, final boolean last) throws Exception
    {
        final WebSocketServiceContextEntry entr = m_collection.get(session.getId());

        if (null != entr)
        {
            return entr.onMessage(text, last);
        }
        return false;
    }

    public boolean addEndPoint(final Session session, final IWebSocketService service)
    {
        final String id = session.getId();

        if (false == m_collection.containsKey(id))
        {
            m_collection.put(id, new WebSocketServiceContextEntry(session, m_context, service));

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

    public boolean broadcast(final String text)
    {
        return broadcast(text, true);
    }

    public boolean broadcast(final JSONObject json)
    {
        return broadcast(json, true);
    }

    public boolean broadcast(final String text, final boolean last)
    {
        for (WebSocketServiceContextEntry entry : m_collection.values())
        {
            entry.reply(text, last);
        }
        return true;
    }

    public boolean broadcast(final JSONObject json, final boolean last)
    {
        String safe = null;

        String text = null;

        for (WebSocketServiceContextEntry entry : m_collection.values())
        {
            if (entry.isStrict())
            {
                if (null == safe)
                {
                    safe = json.toJSONString(true);
                }
                entry.reply(safe, last);
            }
            else
            {
                if (null == text)
                {
                    text = json.toJSONString(false);
                }
                entry.reply(text, last);
            }
        }
        return true;
    }
}
