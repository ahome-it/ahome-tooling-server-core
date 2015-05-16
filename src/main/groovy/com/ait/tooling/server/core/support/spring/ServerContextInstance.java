/*
 * Copyright (c) 2014,2015 Ahome' Innovation Technologies. All rights reserved.
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

package com.ait.tooling.server.core.support.spring;

import java.util.Objects;

import org.apache.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.web.context.WebApplicationContext;

import com.ait.tooling.json.JSONObject;
import com.ait.tooling.server.core.jmx.management.IServerManager;
import com.ait.tooling.server.core.security.AnonOnlyAuthorizationProvider;
import com.ait.tooling.server.core.security.AuthorizationResult;
import com.ait.tooling.server.core.security.IAuthorizationProvider;

public final class ServerContextInstance implements IServerContext
{
    private static final long                          serialVersionUID = 8451400323005323866L;

    private static final Logger                        logger           = Logger.getLogger(ServerContextInstance.class);

    private final static AnonOnlyAuthorizationProvider DEFAULT_AUTH     = new AnonOnlyAuthorizationProvider();

    private final static DefaultPrincipalsKeysProvider DEFAULT_KEYS     = new DefaultPrincipalsKeysProvider();

    private final static ServerContextInstance         INSTANCE         = new ServerContextInstance();

    private WebApplicationContext                      m_context;

    @Override
    public final IServerContext getServerContext()
    {
        return this;
    }

    public static final ServerContextInstance get()
    {
        return INSTANCE;
    }

    private ServerContextInstance()
    {
    }

    public final void setApplicationContext(final WebApplicationContext context)
    {
        m_context = Objects.requireNonNull(context);
    }

    @Override
    public final WebApplicationContext getApplicationContext()
    {
        return m_context;
    }

    @Override
    public final Environment getEnvironment()
    {
        return m_context.getEnvironment();
    }

    public final <T> T getBean(final String name, final Class<T> type)
    {
        return m_context.getBean(Objects.requireNonNull(name), Objects.requireNonNull(type));
    }

    @Override
    public IPropertiesResolver getPropertiesResolver()
    {
        return this;
    }

    @Override
    public final String getPropertyByName(final String name)
    {
        final String valu = getEnvironment().getProperty(Objects.requireNonNull(name));

        if (null != valu)
        {
            return valu;
        }
        return getCorePropertiesResolver().getPropertyByName(name);
    }

    @Override
    public final String getPropertyByName(final String name, final String otherwise)
    {
        final String valu = getEnvironment().getProperty(Objects.requireNonNull(name));

        if (null != valu)
        {
            return valu;
        }
        return getCorePropertiesResolver().getPropertyByName(name, otherwise);
    }

    @Override
    public final IAuthorizationProvider getAuthorizationProvider()
    {
        if (m_context.containsBean("AuthorizationProvider"))
        {
            try
            {
                return getBean("AuthorizationProvider", IAuthorizationProvider.class);
            }
            catch (Exception e)
            {
                logger.error("Invalid AuthorizationProvider", e);
            }
        }
        logger.warn("Using AuthorizationProvider default " + DEFAULT_AUTH.getClass().getName());

        return DEFAULT_AUTH;
    }

    @Override
    public final Iterable<String> getPrincipalsKeys()
    {
        if (m_context.containsBean("PrincipalsKeysProvider"))
        {
            try
            {
                return getBean("PrincipalsKeysProvider", IPrincipalsKeysProvider.class);
            }
            catch (Exception e)
            {
                logger.error("Invalid PrincipalsKeysProvider", e);
            }
        }
        logger.warn("Using PrincipalsKeysProvider default " + DEFAULT_KEYS.getClass().getName());

        return DEFAULT_KEYS;
    }

    @Override
    public final IServerManager getServerManager()
    {
        return getBean("ServerManager", IServerManager.class);
    }

    @Override
    public IExecutorServiceDescriptorProvider getExecutorServiceDescriptorProvider()
    {
        return getBean("ExecutorServiceDescriptorProvider", IExecutorServiceDescriptorProvider.class);
    }

    @Override
    public IBuildDescriptorProvider getBuildDescriptorProvider()
    {
        return getBean("BuildDescriptorProvider", IBuildDescriptorProvider.class);
    }

    private final CorePropertiesResolver getCorePropertiesResolver()
    {
        return getBean("CorePropertiesResolver", CorePropertiesResolver.class);
    }

    @Override
    public AuthorizationResult isAuthorized(Object target, JSONObject principals)
    {
        return getAuthorizationProvider().isAuthorized(target, principals);
    }
}
