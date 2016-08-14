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

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.ait.tooling.server.core.support.spring.ServerContextInstance;

@ServerEndpoint("/sockets/endpoints/{name}")
public class WebSocketEndPointByName extends ServerContextInstance
{
    private IWebSocketServiceProvider m_provider;

    public WebSocketEndPointByName()
    {
        System.err.println("CTOR");
    }

    @OnOpen
    public void onOpen(final Session session, @PathParam("name") final String name)
    {
        logger().info("onOpen");

        if (null == m_provider)
        {
            m_provider = getWebSocketServiceProvider();
        }
        final IWebSocketService service = getWebSocketService(name);

        if (null != service)
        {
            m_provider.addEndPoint(session, name, service);
        }
        else
        {
            logger().error("Can't find IWebSocketService " + name);
        }
    }

    @OnClose
    public void onClose(final Session session, @PathParam("name") final String name)
    {
        logger().info("onClose");

        m_provider.removeEndPoint(session, name);
    }

    @OnMessage
    public void onText(final Session session, final String text, final boolean last)
    {
        logger().info("onText");

        try
        {
            if (session.isOpen())
            {
                m_provider.onMessage(session, session.getPathParameters().get("name"), text, last);
            }
            else
            {
                logger().error("session not open");
            }
        }
        catch (Exception e)
        {
            logger().error("onText()", e);

            try
            {
                session.close();
            }
            catch (Exception i)
            {
                logger().error("onText() session.close()", i);
            }
        }
    }
}