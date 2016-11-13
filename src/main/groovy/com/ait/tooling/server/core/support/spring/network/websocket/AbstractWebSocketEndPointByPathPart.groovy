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

package com.ait.tooling.server.core.support.spring.network.websocket

import java.nio.ByteBuffer

import javax.websocket.CloseReason
import javax.websocket.EndpointConfig
import javax.websocket.PongMessage
import javax.websocket.Session

import com.ait.tooling.common.api.java.util.StringOps

import groovy.transform.CompileStatic
import groovy.transform.Memoized

@CompileStatic
public abstract class AbstractWebSocketEndPointByPathPart extends AbstractWebSocketServiceContext
{
    private final String    m_pathpart

    protected AbstractWebSocketEndPointByPathPart(final String pathpart)
    {
        m_pathpart = StringOps.toTrimOrNull(pathpart)
    }

    public void onOpen(final Session session, final EndpointConfig config) throws IOException
    {
        setSession(session)

        debug("onOpen(${getEndPointName()}, ${getEndPointIden()})")

        final IWebSocketService service = getWebSocketService(getEndPointName())

        if (service)
        {
            setService(service)

            if (false == getWebSocketServiceProvider().addWebSocketServiceSession(this))
            {
                logger().error("onOpen(${getEndPointName()}, ${getEndPointIden()}) Can't add IWebSocketServiceSession")

                try
                {
                    session.close()
                }
                catch (Exception e)
                {
                    logger().error("onOpen(${getEndPointName()}, ${getEndPointIden()}).close()", e)
                }
            }
        }
        else
        {
            logger().error("onOpen(${getEndPointName()}, ${getEndPointIden()}) Can't find WebSocketService")

            try
            {
                session.close()
            }
            catch (Exception e)
            {
                logger().error("onOpen(${getEndPointName()}, ${getEndPointIden()}).close()", e)
            }
        }
    }

    public void onClose(final Session session, final CloseReason reason) throws IOException
    {
        debug("onClose(${getEndPointName()}, ${getEndPointIden()})")

        getWebSocketServiceProvider().removeWebSocketServiceSession(this)
    }

    protected void debug(final String text)
    {
        logger().debug(text)
    }

    public void onText(final Session session, final String text, final boolean last) throws IOException
    {
        try
        {
            if (isOpen())
            {
                final IWebSocketService service = getService()

                if (service)
                {
                    service.onMessage(this, text, last)
                }
                else
                {
                    logger().error("onText(${getEndPointName()}, ${getEndPointIden()}) WebSocketService is null")
                }
            }
            else
            {
                logger().error("onText(${getEndPointName()}, ${getEndPointIden()}) Session is closed")
            }
        }
        catch (Exception e)
        {
            logger().error("onText(${getEndPointName()}, ${getEndPointIden()})", e)

            if (doCloseOnException(e))
            {
                try
                {
                    close()
                }
                catch (Exception i)
                {
                    logger().error("onText(${getEndPointName()}, ${getEndPointIden()}).close()", i)
                }
            }
        }
    }

    public void onError(final Session session, final Throwable t)
    {
        logger().error("onError(${getEndPointName()}, ${getEndPointIden()}) " + t.getMessage())
    }

    public void onBinary(final Session session, final ByteBuffer bb, final boolean last)
    {
        logger().error("onBinary(${getEndPointName()}, ${getEndPointIden()}) unimplemented")
    }

    public void onPongMessage(final PongMessage pm)
    {
        logger().error("onPongMessage(${getEndPointName()}, ${getEndPointIden()}) unimplemented")
    }

    @Memoized
    public String getEndPointIden()
    {
        getId()
    }

    @Memoized
    public String getEndPointName()
    {
        getPathParameter(getPathPart())
    }

    @Memoized
    public String getPathPart()
    {
        m_pathpart
    }

    public boolean doCloseOnException(Exception e)
    {
        true
    }
}
