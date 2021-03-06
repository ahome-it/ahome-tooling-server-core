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

package com.ait.tooling.server.core.support.spring;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.SubscribableChannel;

import com.ait.tooling.common.api.java.util.StringOps;
import com.ait.tooling.server.core.jmx.management.ICoreServerManager;
import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.json.support.JSONUtilitiesInstance;
import com.ait.tooling.server.core.pubsub.JSONMessageBuilder;
import com.ait.tooling.server.core.scripting.IScriptingProvider;
import com.ait.tooling.server.core.security.AuthorizationResult;
import com.ait.tooling.server.core.security.DefaultAuthorizationProvider;
import com.ait.tooling.server.core.security.IAuthorizationProvider;
import com.ait.tooling.server.core.security.ICryptoProvider;
import com.ait.tooling.server.core.security.ISignatoryProvider;
import com.ait.tooling.server.core.security.session.IServerSessionRepository;
import com.ait.tooling.server.core.security.session.IServerSessionRepositoryProvider;
import com.ait.tooling.server.core.support.spring.network.ICoreNetworkProvider;
import com.ait.tooling.server.core.support.spring.network.websocket.IWebSocketService;
import com.ait.tooling.server.core.support.spring.network.websocket.IWebSocketServiceProvider;

public class ServerContextInstance extends JSONUtilitiesInstance implements IServerContext
{
    private static ApplicationContext                  APPCONTEXT   = null;

    private final static DefaultAuthorizationProvider  DEFAULT_AUTH = new DefaultAuthorizationProvider();

    private final static DefaultPrincipalsKeysProvider DEFAULT_KEYS = new DefaultPrincipalsKeysProvider();

    private final static ServerContextInstance         INSTANCE     = new ServerContextInstance();

    private final Logger                               m_logger     = Logger.getLogger(getClass());

    public static final ServerContextInstance getServerContextInstance()
    {
        return INSTANCE;
    }

    protected ServerContextInstance()
    {
    }

    public static final void setApplicationContext(final ApplicationContext context)
    {
        APPCONTEXT = context;
    }

    @Override
    public final boolean isApplicationContextInitialized()
    {
        return (null != APPCONTEXT);
    }

    @Override
    public final ApplicationContext getApplicationContext()
    {
        return Objects.requireNonNull(APPCONTEXT, "ApplicationContext is null, initialization error.");
    }

    @Override
    public final Environment getEnvironment()
    {
        return Objects.requireNonNull(getApplicationContext().getEnvironment(), "Environment is null, initialization error.");
    }

    @Override
    public final boolean containsBean(final String name)
    {
        return getApplicationContext().containsBean(Objects.requireNonNull(name));
    }

    @Override
    public final <B> B getBean(final String name, final Class<B> type) throws Exception
    {
        return getApplicationContext().getBean(Objects.requireNonNull(name), Objects.requireNonNull(type));
    }

    @Override
    public final <B> B getBeanSafely(final String name, final Class<B> type)
    {
        Objects.requireNonNull(name);

        Objects.requireNonNull(type);

        B bean = null;

        try
        {
            final ApplicationContext ctxt = getApplicationContext();

            if (ctxt.containsBean(name))
            {
                try
                {
                    bean = ctxt.getBean(name, type);
                }
                catch (Exception e)
                {
                    logger().error("getBeanSafely(" + name + "," + type.getName() + ") error.", e);
                }
                if (null == bean)
                {
                    try
                    {
                        final Object look = ctxt.getBean(name);

                        if (null != look)
                        {
                            if (type.isAssignableFrom(look.getClass()))
                            {
                                bean = type.cast(look);
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        logger().error("getBeanSafely(" + name + "," + type.getName() + ") error.", e);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger().error("getBeanSafely(" + name + "," + type.getName() + ") error.", e);
        }
        return bean;
    }

    @Override
    public final IPropertiesResolver getPropertiesResolver()
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
        final IAuthorizationProvider auth = getBeanSafely("AuthorizationProvider", IAuthorizationProvider.class);

        if (null != auth)
        {
            return auth;
        }
        logger().trace("Using AuthorizationProvider default " + DEFAULT_AUTH.getClass().getName());

        return DEFAULT_AUTH;
    }

    @Override
    public final List<String> getPrincipalsKeys()
    {
        final IPrincipalsKeysProvider keys = getBeanSafely("PrincipalsKeysProvider", IPrincipalsKeysProvider.class);

        if (null != keys)
        {
            return keys.getPrincipalsKeys();
        }
        logger().trace("Using PrincipalsKeysProvider default " + DEFAULT_KEYS.getClass().getName());

        return DEFAULT_KEYS.getPrincipalsKeys();
    }

    @Override
    public final IServerSessionRepositoryProvider getServerSessionRepositoryProvider()
    {
        return Objects.requireNonNull(getBeanSafely("ServerSessionRepositoryProvider", IServerSessionRepositoryProvider.class), "ServerSessionRepositoryProvider is null, initialization error.");
    }

    @Override
    public final IServerSessionRepository getServerSessionRepository(final String domain)
    {
        return getServerSessionRepositoryProvider().getServerSessionRepository(Objects.requireNonNull(domain));
    }

    @Override
    public final ICoreServerManager getCoreServerManager()
    {
        return Objects.requireNonNull(getBeanSafely("CoreServerManager", ICoreServerManager.class), "CoreServerManager is null, initialization error.");
    }

    @Override
    public final IBuildDescriptorProvider getBuildDescriptorProvider()
    {
        return Objects.requireNonNull(getBeanSafely("BuildDescriptorProvider", IBuildDescriptorProvider.class), "BuildDescriptorProvider is null, initialization error.");
    }

    @Override
    public final ICryptoProvider getCryptoProvider()
    {
        return Objects.requireNonNull(getBeanSafely("CryptoProvider", ICryptoProvider.class), "CryptoProvider is null, initialization error.");
    }

    @Override
    public final ISignatoryProvider getSignatoryProvider()
    {
        return Objects.requireNonNull(getBeanSafely("SignatoryProvider", ISignatoryProvider.class), "SignatoryProvider is null, initialization error.");
    }

    @Override
    public final ICoreNetworkProvider network()
    {
        return Objects.requireNonNull(getBeanSafely("NetworkProvider", ICoreNetworkProvider.class), "NetworkProvider is null, initialization error.");
    }

    private final CorePropertiesResolver getCorePropertiesResolver()
    {
        return Objects.requireNonNull(getBeanSafely("CorePropertiesResolver", CorePropertiesResolver.class), "CorePropertiesResolver is null, initialization error.");
    }

    @Override
    public final AuthorizationResult isAuthorized(final Object target, final List<String> roles)
    {
        return getAuthorizationProvider().isAuthorized(target, roles);
    }

    @Override
    public final MessageChannel getMessageChannel(final String name)
    {
        MessageChannel channel = getBeanSafely(Objects.requireNonNull(name), MessageChannel.class);

        if (null != channel)
        {
            return channel;
        }
        channel = getSubscribableChannel(name);

        if (null != channel)
        {
            return channel;
        }
        return getPollableChannel(name);
    }

    @Override
    public final PublishSubscribeChannel getPublishSubscribeChannel(final String name)
    {
        return getBeanSafely(Objects.requireNonNull(name), PublishSubscribeChannel.class);
    }

    @Override
    public final SubscribableChannel getSubscribableChannel(final String name)
    {
        final SubscribableChannel channel = getBeanSafely(Objects.requireNonNull(name), SubscribableChannel.class);

        if (null != channel)
        {
            return channel;
        }
        return getPublishSubscribeChannel(name);
    }

    @Override
    public final PollableChannel getPollableChannel(final String name)
    {
        return getBeanSafely(Objects.requireNonNull(name), PollableChannel.class);
    }

    @Override
    public final boolean publish(final String name, final JSONObject message)
    {
        return publish(Objects.requireNonNull(name), JSONMessageBuilder.createMessage(Objects.requireNonNull(message)));
    }

    @Override
    public final boolean publish(final String name, final JSONObject message, final long timeout)
    {
        return publish(Objects.requireNonNull(name), JSONMessageBuilder.createMessage(Objects.requireNonNull(message)), timeout);
    }

    @Override
    public final boolean publish(final String name, final JSONObject message, final Map<String, ?> headers)
    {
        return publish(Objects.requireNonNull(name), JSONMessageBuilder.createMessage(Objects.requireNonNull(message), Objects.requireNonNull(headers)));
    }

    @Override
    public final boolean publish(final String name, final JSONObject message, final Map<String, ?> headers, final long timeout)
    {
        return publish(Objects.requireNonNull(name), JSONMessageBuilder.createMessage(Objects.requireNonNull(message), Objects.requireNonNull(headers)), timeout);
    }

    @Override
    public final <T> boolean publish(final String name, final Message<T> message)
    {
        final MessageChannel channel = getMessageChannel(Objects.requireNonNull(name));

        if (null != channel)
        {
            return channel.send(Objects.requireNonNull(message));
        }
        throw new IllegalArgumentException("MessageChannel " + name + " does not exist.");
    }

    @Override
    public final <T> boolean publish(final String name, final Message<T> message, final long timeout)
    {
        final MessageChannel channel = getMessageChannel(Objects.requireNonNull(name));

        if (null != channel)
        {
            return channel.send(Objects.requireNonNull(message), timeout);
        }
        throw new IllegalArgumentException("MessageChannel " + name + " does not exist.");
    }

    @Override
    public final String uuid()
    {
        return UUID.randomUUID().toString().toUpperCase();
    }

    @Override
    public Logger logger()
    {
        return m_logger;
    }

    @Override
    public final String toTrimOrNull(final String string)
    {
        return StringOps.toTrimOrNull(string);
    }

    @Override
    public final String toTrimOrElse(final String string, final String otherwise)
    {
        return StringOps.toTrimOrElse(string, otherwise);
    }

    @Override
    public final <T> T requireNonNull(final T object)
    {
        return Objects.requireNonNull(object);
    }

    @Override
    public final <T> T requireNonNull(final T object, final String message)
    {
        return Objects.requireNonNull(object, message);
    }

    @Override
    public final IScriptingProvider scripting()
    {
        return Objects.requireNonNull(getBeanSafely("ScriptingProvider", IScriptingProvider.class), "ScriptingProvider is null, initialization error.");
    }

    @Override
    public final Resource resource(final String location)
    {
        return getApplicationContext().getResource(Objects.requireNonNull(location));
    }

    @Override
    public final Reader reader(final String location) throws IOException
    {
        final Resource resource = resource(Objects.requireNonNull(location));

        if (null != resource)
        {
            return new InputStreamReader(resource.getInputStream());
        }
        return null;
    }

    @Override
    public final IWebSocketServiceProvider getWebSocketServiceProvider()
    {
        return Objects.requireNonNull(getBeanSafely("WebSocketServiceProvider", IWebSocketServiceProvider.class), "WebSocketServiceProvider is null, initialization error.");
    }

    @Override
    public final IWebSocketService getWebSocketService(final String name)
    {
        return getWebSocketServiceProvider().getWebSocketService(name);
    }
}
