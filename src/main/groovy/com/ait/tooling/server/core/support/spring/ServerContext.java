/*
   Copyright (c) 2014,2015 Ahome' Innovation Technologies. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.ait.tooling.server.core.support.spring;

import java.util.Objects;

import org.springframework.core.env.Environment;
import org.springframework.web.context.WebApplicationContext;

import com.ait.tooling.server.core.jmx.management.IServerManager;
import com.ait.tooling.server.core.rpc.IJSONCommand;
import com.ait.tooling.server.core.security.IAuthorizationProvider;
import com.ait.tooling.server.core.security.AnonOnlyAuthorizationProvider;

public final class ServerContext
{
    private static final ServerContext    INSTANCE  = new ServerContext();

    private WebApplicationContext         m_context;

    private final IAuthorizationProvider  m_authpro = new AnonOnlyAuthorizationProvider();

    private final IPrincipalsKeysProvider m_keyspro = new DefaultPrincipalsKeysProvider();

    public static final ServerContext get()
    {
        return INSTANCE;
    }

    private ServerContext()
    {
    }

    public final void setApplicationContext(final WebApplicationContext context)
    {
        m_context = Objects.requireNonNull(context);
    }

    public final WebApplicationContext getApplicationContext()
    {
        return m_context;
    }

    public final Environment getEnvironment()
    {
        return m_context.getEnvironment();
    }

    public final <T> T getBean(final String name, final Class<T> type)
    {
        return m_context.getBean(Objects.requireNonNull(name), Objects.requireNonNull(type));
    }

    public final ICommandRegistry getCommandRegistry()
    {
        return getBean("CommandRegistry", ICommandRegistry.class);
    }

    public final IJSONCommand getCommand(final String name)
    {
        return getCommandRegistry().getCommand(Objects.requireNonNull(name));
    }

    public final IPropertiesProvider getPropertiesProvider()
    {
        return getBean("PropertiesProvider", IPropertiesProvider.class);
    }

    public final String getPropertyByName(final String name)
    {
        return getPropertiesProvider().getPropertyByName(Objects.requireNonNull(name));
    }

    public final String getPropertyByName(final String name, final String otherwise)
    {
        return getPropertiesProvider().getPropertyByName(Objects.requireNonNull(name), otherwise);
    }

    public final IAuthorizationProvider getAuthorizationProvider()
    {
        final IAuthorizationProvider auth = getBean("AuthorizationProvider", IAuthorizationProvider.class);

        if (null == auth)
        {
            return m_authpro;
        }
        return auth;
    }

    public final Iterable<String> getPrincipalsKeys()
    {
        final Iterable<String> iter = getBean("PrincipalsKeysProvider", IPrincipalsKeysProvider.class);

        if (null == iter)
        {
            return m_keyspro;
        }
        return iter;
    }

    public final IServerManager getServerManager()
    {
        return getBean("ServerManager", IServerManager.class);
    }
}
