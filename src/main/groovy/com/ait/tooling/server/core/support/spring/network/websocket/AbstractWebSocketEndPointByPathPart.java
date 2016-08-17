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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.websocket.PongMessage;
import javax.websocket.Session;

import org.apache.log4j.Logger;

import com.ait.tooling.common.api.java.util.StringOps;
import com.ait.tooling.server.core.support.CoreGroovySupport;
import com.ait.tooling.server.core.support.spring.IServerContext;

public abstract class AbstractWebSocketEndPointByPathPart
{
    private final String                    m_pathpart;

    private final IWebSocketServiceProvider m_provider;

    private final Set<String>               m_scopes = new HashSet<String>();

    private final Logger                    m_logger = Logger.getLogger(getClass());

    protected AbstractWebSocketEndPointByPathPart(final String pathpart)
    {
        m_pathpart = pathpart;

        m_provider = getServerContext().getWebSocketServiceProvider();
    }

    public IServerContext getServerContext()
    {
        return CoreGroovySupport.getCoreGroovySupport();
    }

    public Logger logger()
    {
        return m_logger;
    }

    public void addScope(final String scope)
    {
        m_scopes.add(scope);
    }

    public void setScopes(final Collection<String> scopes)
    {
        m_scopes.clear();

        m_scopes.addAll(scopes);
    }

    public void setScopes(String... scopes)
    {
        setScopes(Arrays.asList(scopes));
    }

    public List<String> getScopes()
    {
        return Collections.unmodifiableList(new ArrayList<String>(m_scopes));
    }

    public void onOpen(final Session session)
    {
        final String name = getEndPointName(session);

        final IWebSocketService service = getServerContext().getWebSocketService(name, getScopes());

        if (null != service)
        {
            m_provider.addEndPoint(session, name, service);
        }
        else
        {
            logger().error("onOpen(" + name + ") Can't find WebSocketService in " + StringOps.toPrintableString(getScopes()));
        }
    }

    public void onClose(final Session session)
    {
        m_provider.removeEndPoint(session, getEndPointName(session));
    }

    public void onText(final Session session, final String text, final boolean last)
    {
        final String name = getEndPointName(session);

        try
        {
            if (session.isOpen())
            {
                m_provider.onMessage(session, name, text, last);
            }
            else
            {
                logger().error("onText(" + name + ") Session is closed");
            }
        }
        catch (Exception e)
        {
            logger().error("onText(" + name + ")", e);

            if (doCloseOnException(e))
            {
                try
                {
                    session.close();
                }
                catch (Exception i)
                {
                }
            }
        }
    }

    public void onBinary(final Session session, final ByteBuffer bb, final boolean last)
    {
    }

    public void onPongMessage(final PongMessage pm)
    {
    }

    public String getEndPointName(final Session session)
    {
        return getPathParameter(session, getPathPart());
    }

    public String getPathParameter(final Session session, final String name)
    {
        return session.getPathParameters().get(name);
    }

    public String getPathPart()
    {
        return m_pathpart;
    }

    public boolean doCloseOnException(Exception e)
    {
        return true;
    }
}