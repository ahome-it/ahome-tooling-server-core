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

package com.ait.tooling.server.core.socket;

import java.io.IOException;
import java.util.LinkedHashMap;

import javax.websocket.Session;

import com.ait.tooling.server.core.support.spring.IServerContext;
import com.ait.tooling.server.core.support.spring.ServerContextInstance;

public class WebSocketServiceCollection
{
    private final IServerContext m_context;
    
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
        for (WebSocketServiceContextEntry entry : m_collection.values())
        {
            try
            {
                entry.getRemoteBasic().sendText(text, true);
            }
            catch (IOException e)
            {
            }
        }
        return true;
    }
}
