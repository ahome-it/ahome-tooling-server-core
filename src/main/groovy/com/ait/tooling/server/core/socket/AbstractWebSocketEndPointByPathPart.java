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

import java.nio.ByteBuffer;

import javax.websocket.PongMessage;
import javax.websocket.Session;

import org.apache.log4j.Logger;

import com.ait.tooling.server.core.support.CoreGroovySupport;
import com.ait.tooling.server.core.support.spring.IServerContext;

public abstract class AbstractWebSocketEndPointByPathPart
{
    private String                    m_pathpart;

    private IWebSocketServiceProvider m_provider;

    private Logger                    m_logger = Logger.getLogger(getClass());

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

    public void onOpen(final Session session)
    {
        final String name = getEndPointName(session);

        final IWebSocketService service = getServerContext().getWebSocketService(name);

        if (null != service)
        {
            m_provider.addEndPoint(session, name, service);
        }
        else
        {
            logger().error("onOpen(" + name + ") Can't find WebSocketService");
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
        return session.getPathParameters().get(getPathPart());
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