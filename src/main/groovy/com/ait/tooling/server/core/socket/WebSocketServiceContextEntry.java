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
import java.util.Objects;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

import com.ait.tooling.server.core.support.spring.IServerContext;

public class WebSocketServiceContextEntry implements IWebSocketServiceContextEntry
{
    private final Session                   m_session;

    private final IServerContext            m_context;

    private final IWebSocketServiceProvider m_providr;

    private final IWebSocketService         m_service;

    private final RemoteEndpoint.Async      m_reasync;

    private final RemoteEndpoint.Basic      m_rebasic;

    public WebSocketServiceContextEntry(final Session session, final IServerContext context, final IWebSocketService service)
    {
        m_service = Objects.requireNonNull(service);

        m_session = Objects.requireNonNull(session);

        m_context = Objects.requireNonNull(context);
        
        m_providr = m_context.getWebSocketServiceProvider();

        m_reasync = m_session.getAsyncRemote();

        m_rebasic = m_session.getBasicRemote();
    }

    @Override
    public Session getSession()
    {
        return m_session;
    }

    @Override
    public RemoteEndpoint.Async getRemoteAsync()
    {
        return m_reasync;
    }

    @Override
    public RemoteEndpoint.Basic getRemoteBasic()
    {
        return m_rebasic;
    }

    @Override
    public boolean isOpen()
    {
        return getSession().isOpen();
    }

    @Override
    public void close() throws IOException
    {
        Session session = getSession();

        if (session.isOpen())
        {
            session.close();
        }
    }

    @Override
    public boolean onMessage(final String text, final boolean last) throws Exception
    {
        return m_service.onMessage(this, text, last);
    }

    @Override
    public IWebSocketService getSerivce()
    {
        return m_service;
    }

    @Override
    public boolean broadcast(final String name, final String text)
    {
        return m_providr.broadcast(name, text);
    }

    @Override
    public IServerContext getServerContext()
    {
        return m_context;
    }

    @Override
    public boolean reply(final String text)
    {
        try
        {
            getRemoteBasic().sendText(text, true);
            
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }
}
