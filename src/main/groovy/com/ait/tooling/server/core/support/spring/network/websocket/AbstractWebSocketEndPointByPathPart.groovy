/*
 * Copyright (c) 2017 Ahome' Innovation Technologies. All rights reserved.
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

package com.ait.tooling.server.core.support.spring.network.websocket

import java.nio.ByteBuffer

import javax.websocket.CloseReason
import javax.websocket.EndpointConfig
import javax.websocket.PongMessage
import javax.websocket.Session

import com.ait.tooling.common.api.java.util.StringOps
import com.ait.tooling.server.core.support.CoreGroovySupport

import groovy.transform.CompileStatic

@CompileStatic
public abstract class AbstractWebSocketEndPointByPathPart extends CoreGroovySupport
{
    private final String                m_pathpart

    private WebSocketServiceContext     m_context

    protected AbstractWebSocketEndPointByPathPart(final String pathpart)
    {
        m_pathpart = StringOps.toTrimOrNull(pathpart)
    }

    public void onOpen(final Session session, final EndpointConfig config) throws IOException
    {
        final String name = getEndPointName(session)

        final String iden = getEndPointIden(session)

        final IWebSocketService service = getWebSocketService(name)

        if (service)
        {
            logger().info("onOpen(${name},${iden})")

            m_context = new WebSocketServiceContext(session, service)

            getWebSocketServiceProvider().addWebSocketServiceSession(m_context)
        }
        else
        {
            logger().error("onOpen(${name},${iden}) Can't find WebSocketService")

            try
            {
                session.close()
            }
            catch (Exception e)
            {
                logger().error("onOpen(${name},${iden}).close()", e)
            }
        }
    }

    public void onClose(final Session session, final CloseReason reason) throws IOException
    {
        final String name = getEndPointName(session)

        final String iden = getEndPointIden(session)

        logger().info("onClose(${name},${iden})")

        if (m_context)
        {
            getWebSocketServiceProvider().removeWebSocketServiceSession(m_context)
        }
    }

    public void onText(final Session session, final String text, final boolean last) throws IOException
    {
        final String name = getEndPointName(session)

        final String iden = getEndPointIden(session)

        try
        {
            if (session.isOpen())
            {
                m_context.getService().onMessage(m_context, text, last)
            }
            else
            {
                logger().error("onText(${name},${iden}) Session is closed")
            }
        }
        catch (Exception e)
        {
            logger().error("onText(${name},${iden})", e)

            if (doCloseOnException(e))
            {
                try
                {
                    session.close()
                }
                catch (Exception i)
                {
                    logger().error("onText(${name},${iden}).close()", i)
                }
            }
        }
    }

    public void onError(final Session session, final Throwable t)
    {
        final String name = getEndPointName(session)

        final String iden = getEndPointIden(session)

        logger().error("onError(${name},${iden}): " + t.getMessage())
    }

    public void onBinary(final Session session, final ByteBuffer bb, final boolean last)
    {
    }

    public void onPongMessage(final PongMessage pm)
    {
    }

    public String getEndPointIden(final Session session)
    {
        session.getId()
    }

    public String getEndPointName(final Session session)
    {
        getPathParameter(session, getPathPart())
    }

    public String getPathParameter(final Session session, final String name)
    {
        session.getPathParameters().get(name)
    }

    public String getPathPart()
    {
        m_pathpart
    }

    public boolean doCloseOnException(Exception e)
    {
        true
    }
}