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
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import javax.websocket.Session;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import com.ait.tooling.common.api.java.util.StringOps;

public class WebSocketServiceProvider implements IWebSocketServiceProvider, BeanFactoryAware
{
    private static final Logger                            logger     = Logger.getLogger(WebSocketServiceProvider.class);

    private final LinkedHashMap<String, IWebSocketService> m_services = new LinkedHashMap<String, IWebSocketService>();

    private final WebSocketEndPointCollection              m_endpoint = new WebSocketEndPointCollection();

    public WebSocketServiceProvider()
    {
    }

    protected void addService(final IWebSocketService service)
    {
        if (null != service)
        {
            final String name = StringOps.toTrimOrNull(service.getName());

            if (null != name)
            {
                if (null == m_services.get(name))
                {
                    m_services.put(name, service);

                    logger.info("WebSocketServiceProvider.addService(" + name + ") Registered");
                }
                else
                {
                    logger.error("WebSocketServiceProvider.addService(" + name + ") Duplicate ignored");
                }
            }
        }
    }

    @Override
    public IWebSocketService getWebSocketService(final String name)
    {
        return m_services.get(StringOps.requireTrimOrNull(name));
    }

    @Override
    public List<String> getWebSocketServiceNames()
    {
        return Collections.unmodifiableList(new ArrayList<String>(m_services.keySet()));
    }

    @Override
    public List<IWebSocketService> getWebSocketServices()
    {
        return Collections.unmodifiableList(new ArrayList<IWebSocketService>(m_services.values()));
    }

    @Override
    public void setBeanFactory(final BeanFactory factory) throws BeansException
    {
        if (factory instanceof DefaultListableBeanFactory)
        {
            for (IWebSocketService service : ((DefaultListableBeanFactory) factory).getBeansOfType(IWebSocketService.class).values())
            {
                addService(service);
            }
        }
    }

    @Override
    public void close() throws IOException
    {
        for (IWebSocketService service : m_services.values())
        {
            if (null != service)
            {
                try
                {
                    service.close();
                }
                catch (Exception e)
                {
                    logger.error(service.getName(), e);
                }
            }
        }
    }

    @Override
    public boolean broadcast(final String name, final String text)
    {
        return m_endpoint.broadcast(name, text);
    }

    @Override
    public boolean broadcast(final String name, final String text, final boolean last)
    {
        return m_endpoint.broadcast(name, text, last);
    }

    @Override
    public boolean addEndPoint(final Session session, final String name, final IWebSocketService service)
    {
        return m_endpoint.addEndPoint(session, name, service);
    }

    @Override
    public boolean removeEndPoint(final Session session, final String name)
    {
        return m_endpoint.removeEndPoint(session, name);
    }

    @Override
    public boolean onMessage(final Session session, final String name, final String text, final boolean last) throws Exception
    {
        return m_endpoint.onMessage(session, name, text, last);
    }
}
