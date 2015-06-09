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

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

import com.ait.tooling.json.JSONArray;
import com.ait.tooling.json.JSONObject;
import com.ait.tooling.json.parser.JSONParser;
import com.ait.tooling.json.parser.JSONParserException;
import com.ait.tooling.json.schema.JSONSchema;
import com.ait.tooling.server.core.jmx.management.ICoreServerManager;
import com.ait.tooling.server.core.security.AnonOnlyAuthorizationProvider;
import com.ait.tooling.server.core.security.AuthorizationResult;
import com.ait.tooling.server.core.security.IAuthorizationProvider;
import com.ait.tooling.server.core.security.ICryptoProvider;

public class ServerContextInstance implements IServerContext
{
    private static final long                          serialVersionUID    = 8451400323005323866L;

    private static ApplicationContext                  APPLICATION_CONTEXT = null;

    private final static AnonOnlyAuthorizationProvider DEFAULT_AUTH        = new AnonOnlyAuthorizationProvider();

    private final static DefaultPrincipalsKeysProvider DEFAULT_KEYS        = new DefaultPrincipalsKeysProvider();

    private final static ServerContextInstance         INSTANCE            = new ServerContextInstance();

    private final Logger                               m_logger            = Logger.getLogger(getClass());

    @Override
    public final IServerContext getServerContext()
    {
        return this;
    }

    public static final ServerContextInstance getServerContextInstance()
    {
        return INSTANCE;
    }

    protected ServerContextInstance()
    {
    }

    public final void setApplicationContext(final ApplicationContext context)
    {
        APPLICATION_CONTEXT = Objects.requireNonNull(context);
    }

    @Override
    public final ApplicationContext getApplicationContext()
    {
        return Objects.requireNonNull(APPLICATION_CONTEXT, "ApplicationContext is null, initialization error.");
    }

    @Override
    public final Environment getEnvironment()
    {
        return Objects.requireNonNull(getApplicationContext().getEnvironment(), "Environment is null, initialization error.");
    }

    @Override
    public boolean containsBean(final String name)
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
        logger().warn("Using AuthorizationProvider default " + DEFAULT_AUTH.getClass().getName());

        return DEFAULT_AUTH;
    }

    @Override
    public final Iterable<String> getPrincipalsKeys()
    {
        final IPrincipalsKeysProvider keys = getBeanSafely("PrincipalsKeysProvider", IPrincipalsKeysProvider.class);

        if (null != keys)
        {
            return keys;
        }
        logger().warn("Using PrincipalsKeysProvider default " + DEFAULT_KEYS.getClass().getName());

        return DEFAULT_KEYS;
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

    private final CorePropertiesResolver getCorePropertiesResolver()
    {
        return Objects.requireNonNull(getBeanSafely("CorePropertiesResolver", CorePropertiesResolver.class), "CorePropertiesResolver is null, initialization error.");
    }

    @Override
    public final AuthorizationResult isAuthorized(final Object target, final JSONObject principals)
    {
        return getAuthorizationProvider().isAuthorized(target, principals);
    }

    @Override
    public MessageChannel getMessageChannel(final String name)
    {
        final MessageChannel channel = getBeanSafely(Objects.requireNonNull(name), MessageChannel.class);

        if (null != channel)
        {
            return channel;
        }
        return getPublishSubscribeChannel(name);
    }

    @Override
    public PublishSubscribeChannel getPublishSubscribeChannel(String name)
    {
        return getBeanSafely(Objects.requireNonNull(name), PublishSubscribeChannel.class);
    }

    @Override
    public SubscribableChannel getSubscribableChannel(String name)
    {
        final SubscribableChannel channel = getBeanSafely(Objects.requireNonNull(name), SubscribableChannel.class);

        if (null != channel)
        {
            return channel;
        }
        return getPublishSubscribeChannel(name);
    }

    @Override
    public <T> boolean publish(final String name, final Message<T> message)
    {
        final MessageChannel channel = getMessageChannel(Objects.requireNonNull(name));

        if (null != channel)
        {
            return channel.send(Objects.requireNonNull(message));
        }
        throw new IllegalArgumentException("MessageChannel " + name + " does not exist.");
    }

    @Override
    public <T> boolean publish(final String name, final Message<T> message, final long timeout)
    {
        final MessageChannel channel = getMessageChannel(Objects.requireNonNull(name));

        if (null != channel)
        {
            return channel.send(Objects.requireNonNull(message), timeout);
        }
        throw new IllegalArgumentException("MessageChannel " + name + " does not exist.");
    }

    @Override
    public final JSONObject json()
    {
        return new JSONObject();
    }

    @Override
    public final JSONObject json(final Map<String, ?> map)
    {
        return new JSONObject(Objects.requireNonNull(map));
    }

    @Override
    public final JSONObject json(final String name, final Object value)
    {
        return new JSONObject(Objects.requireNonNull(name), value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final JSONObject json(final Collection<?> collection)
    {
        Objects.requireNonNull(collection);

        if (collection instanceof List)
        {
            return json((List<?>) collection);
        }
        else if (collection instanceof Map)
        {
            return json((Map<String, ?>) collection);
        }
        else
        {
            return json(new ArrayList<Object>(collection));
        }
    }

    @Override
    public final JSONObject json(final List<?> list)
    {
        return new JSONObject(Objects.requireNonNull(list));
    }

    @Override
    public final String uuid()
    {
        return UUID.randomUUID().toString().toUpperCase();
    }

    @Override
    public final JSONSchema jsonSchema(final Map<String, ?> schema)
    {
        return JSONSchema.cast(json(Objects.requireNonNull(schema)));
    }

    @Override
    public Logger logger()
    {
        return m_logger;
    }

    @Override
    public final JSONObject jsonParse(final String string) throws JSONParserException
    {
        Objects.requireNonNull(string);

        final Object result = new JSONParser().parse(string);

        if ((null != result) && (result instanceof JSONObject))
        {
            return ((JSONObject) result);
        }
        return null;
    }

    @Override
    public final JSONObject jsonParse(final Reader reader) throws IOException, JSONParserException
    {
        Objects.requireNonNull(reader);

        final Object result = new JSONParser().parse(reader);

        if ((null != result) && (result instanceof JSONObject))
        {
            return ((JSONObject) result);
        }
        return null;
    }

    @Override
    public JSONArray jarr()
    {
        return new JSONArray();
    }

    @Override
    public JSONArray jarr(final JSONObject object)
    {
        Objects.requireNonNull(object);

        final JSONArray list = jarr();

        jarr().add(object);

        return list;
    }

    @Override
    public JSONArray jarr(final List<?> list)
    {
        return new JSONArray(Objects.requireNonNull(list));
    }

    @Override
    public JSONArray jarr(final Map<String, ?> map)
    {
        return jarr(new JSONObject(Objects.requireNonNull(map)));
    }

    @Override
    public JSONArray jarr(final String name, final Object value)
    {
        return jarr(new JSONObject(Objects.requireNonNull(name), value));
    }

    @Override
    @SuppressWarnings("unchecked")
    public JSONArray jarr(final Collection<?> collection)
    {
        Objects.requireNonNull(collection);

        if (collection instanceof List)
        {
            return jarr((List<?>) collection);
        }
        else if (collection instanceof Map)
        {
            return jarr((Map<String, ?>) collection);
        }
        else
        {
            return jarr(new ArrayList<Object>(collection));
        }
    }
}
