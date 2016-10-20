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

import java.io.IOException;
import java.util.Objects;

import javax.websocket.Session;

import com.ait.tooling.common.api.java.util.IHTTPConstants;
import com.ait.tooling.server.core.json.JSONArray;
import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.support.spring.ServerContextInstance;

public class WebSocketServiceContext extends ServerContextInstance implements IWebSocketServiceContext
{
    private final Session                   m_session;

    private final String                    m_subprot;

    private final boolean                   m_stricts;

    private final IWebSocketServiceProvider m_providr;

    private final IWebSocketService         m_service;

    private final IWebSocketServiceContext  m_selfref;

    public WebSocketServiceContext(final Session session, final IWebSocketService service)
    {
        m_service = Objects.requireNonNull(service);

        m_session = Objects.requireNonNull(session);

        m_providr = ServerContextInstance.getServerContextInstance().getWebSocketServiceProvider();

        m_subprot = m_session.getNegotiatedSubprotocol();

        if (null != m_subprot)
        {
            if (m_subprot.contains(IHTTPConstants.X_STRICT_JSON_FORMAT_HEADER))
            {
                m_stricts = true;
            }
            else
            {
                m_stricts = false;
            }
        }
        else
        {
            m_stricts = false;
        }
        m_selfref = this;

        m_service.onOpen(m_selfref);
    }

    @Override
    public Session getSession()
    {
        return m_session;
    }

    @Override
    public boolean isOpen()
    {
        return getSession().isOpen();
    }

    @Override
    public void close() throws IOException
    {
        final Session session = getSession();

        if (session.isOpen())
        {
            session.close();
        }
    }

    public void onMessage(final String text, final boolean last) throws Exception
    {
        m_service.onMessage(m_selfref, text, last);
    }

    @Override
    public IWebSocketService getSerivce()
    {
        return m_service;
    }

    @Override
    public void broadcast(final String name, final String text)
    {
        m_providr.broadcast(name, text);
    }

    @Override
    public void broadcast(final String name, final String text, final boolean last)
    {
        m_providr.broadcast(name, text, last);
    }

    @Override
    public void reply(final String text)
    {
        reply(text, true);
    }

    @Override
    public void reply(final String text, final boolean last)
    {
        try
        {
            final Session sess = getSession();

            synchronized (sess)
            {
                sess.getBasicRemote().sendText(text, last);
            }
        }
        catch (IOException e)
        {
        }
    }

    @Override
    public void broadcast(final String name, final JSONObject json)
    {
        m_providr.broadcast(name, json);
    }

    @Override
    public void reply(final JSONObject json)
    {
        reply(json.toJSONString(isStrict()));
    }

    @Override
    public void reply(final JSONArray batch)
    {
        reply(batch.toJSONString(isStrict()));
    }

    @Override
    public String getPathParameter(final String name)
    {
        return getSession().getPathParameters().get(name);
    }

    @Override
    public boolean isStrict()
    {
        return m_stricts;
    }

    @Override
    public String getId()
    {
        return getSession().getId();
    }

}
