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
import com.ait.tooling.server.core.json.JSONObject;

public class WebSocketServiceProvider implements IWebSocketServiceProvider, BeanFactoryAware
{
    private static final Logger                                   logger     = Logger.getLogger(WebSocketServiceProvider.class);

    private final LinkedHashMap<String, IWebSocketServiceFactory> m_factorys = new LinkedHashMap<String, IWebSocketServiceFactory>();

    private final WebSocketEndPointCollection                     m_endpoint = new WebSocketEndPointCollection();

    public WebSocketServiceProvider()
    {
    }

    protected void addFactory(final String name, final IWebSocketServiceFactory fact)
    {
        if (null != fact)
        {
            if (null != name)
            {
                if (null == m_factorys.get(name))
                {
                    m_factorys.put(name, fact);

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
        final IWebSocketServiceFactory fact = m_factorys.get(StringOps.requireTrimOrNull(name));

        if (null != fact)
        {
            return fact.get();
        }
        return null;
    }

    @Override
    public List<String> getWebSocketServiceNames()
    {
        return Collections.unmodifiableList(new ArrayList<String>(m_factorys.keySet()));
    }

    @Override
    public void setBeanFactory(final BeanFactory factory) throws BeansException
    {
        if (factory instanceof DefaultListableBeanFactory)
        {
            for (String bean : ((DefaultListableBeanFactory) factory).getBeanNamesForType(IWebSocketService.class))
            {
                final IWebSocketService service = factory.getBean(bean, IWebSocketService.class);

                if (factory.isPrototype(bean))
                {
                    addFactory(service.getName(), new IWebSocketServiceFactory()
                    {
                        @Override
                        public IWebSocketService get()
                        {
                            return factory.getBean(bean, IWebSocketService.class);
                        }

                        @Override
                        public boolean isPrototype()
                        {
                            return true;
                        }
                    });
                }
                else
                {
                    addFactory(service.getName(), new IWebSocketServiceFactory()
                    {
                        @Override
                        public IWebSocketService get()
                        {
                            return service;
                        }

                        @Override
                        public boolean isPrototype()
                        {
                            return false;
                        }
                    });
                }
            }
        }
    }

    @Override
    public void close() throws IOException
    {
        for (IWebSocketServiceFactory fact : m_factorys.values())
        {
            if (null != fact)
            {
                if (false == fact.isPrototype())
                {
                    final IWebSocketService sock = fact.get();

                    try
                    {
                        sock.close();
                    }
                    catch (Exception e)
                    {
                        logger.error(sock.getName(), e);
                    }
                }
            }
        }
        m_endpoint.close();
    }

    @Override
    public void broadcast(final String name, final String text)
    {
        m_endpoint.broadcast(name, text);
    }

    @Override
    public void broadcast(final String name, final String text, final boolean last)
    {
        m_endpoint.broadcast(name, text, last);
    }

    @Override
    public void broadcast(final String name, final JSONObject json)
    {
        m_endpoint.broadcast(name, json);
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
    public void onMessage(final Session session, final String name, final String text, final boolean last) throws Exception
    {
        m_endpoint.onMessage(session, name, text, last);
    }

    @Override
    public IWebSocketServiceSession getWebSocketServiceSession(final String id)
    {
        return m_endpoint.getWebSocketServiceSession(id);
    }
}
