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

import javax.websocket.RemoteEndpoint;
import javax.websocket.SendHandler;
import javax.websocket.SendResult;
import javax.websocket.Session;

import com.ait.tooling.common.api.java.util.IHTTPConstants;
import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.support.spring.IServerContext;

public class WebSocketServiceContextEntry implements IWebSocketServiceContextEntry
{
    private final Session                   m_session;

    private final String                    m_subprot;

    private final boolean                   m_stricts;

    private final IServerContext            m_context;

    private final IWebSocketServiceProvider m_providr;

    private final IWebSocketService         m_service;

    private final RemoteEndpoint.Async      m_reasync;

    private final RemoteEndpoint.Basic      m_rebasic;

    private final IWebSocketServiceContext  m_selfref;

    public WebSocketServiceContextEntry(final Session session, final IServerContext context, final IWebSocketService service)
    {
        m_service = Objects.requireNonNull(service);

        m_session = Objects.requireNonNull(session);

        m_context = Objects.requireNonNull(context);

        m_providr = m_context.getWebSocketServiceProvider();

        m_reasync = m_session.getAsyncRemote();

        m_rebasic = m_session.getBasicRemote();

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
        return m_service.onMessage(m_selfref, text, last);
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
    public boolean broadcast(final String name, final String text, final boolean last)
    {
        return m_providr.broadcast(name, text, last);
    }

    @Override
    public IServerContext getServerContext()
    {
        return m_context;
    }

    @Override
    public boolean reply(final String text)
    {
        return reply(text, true);
    }

    @Override
    public boolean reply(final String text, final boolean last)
    {
        try
        {
            getRemoteBasic().sendText(text, last);

            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }

    @Override
    public boolean broadcast(final String name, final JSONObject json)
    {
        return m_providr.broadcast(name, json);
    }

    @Override
    public boolean broadcast(final String name, final JSONObject json, final boolean last)
    {
        return m_providr.broadcast(name, json, last);
    }

    @Override
    public boolean reply(final JSONObject json)
    {
        return reply(json.toJSONString(isStrict()));
    }

    @Override
    public boolean reply(final JSONObject json, final boolean last)
    {
        return reply(json.toJSONString(isStrict()), last);
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
    public boolean reply(final String text, final IWebSocketAsyncCallback<String> callback)
    {
        getRemoteAsync().sendText(text, new SendHandler()
        {
            @Override
            public void onResult(final SendResult result)
            {
                if (result.isOK())
                {
                    callback.onSuccess(m_selfref, text);
                }
                else
                {
                    callback.onFailure(m_selfref, text, result.getException());
                }
            }
        });
        return true;
    }

    @Override
    public boolean reply(final JSONObject json, final IWebSocketAsyncCallback<JSONObject> callback)
    {
        getRemoteAsync().sendText(json.toJSONString(isStrict()), new SendHandler()
        {
            @Override
            public void onResult(final SendResult result)
            {
                if (result.isOK())
                {
                    callback.onSuccess(m_selfref, json);
                }
                else
                {
                    callback.onFailure(m_selfref, json, result.getException());
                }
            }
        });
        return true;
    }
}
