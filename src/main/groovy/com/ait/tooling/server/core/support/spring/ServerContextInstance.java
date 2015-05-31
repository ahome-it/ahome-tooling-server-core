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

import com.ait.tooling.json.JSONObject;
import com.ait.tooling.json.parser.JSONParser;
import com.ait.tooling.json.parser.JSONParserException;
import com.ait.tooling.json.schema.JSONSchema;
import com.ait.tooling.server.core.jmx.management.ICoreServerManager;
import com.ait.tooling.server.core.pubsub.IPubSubDescriptorProvider;
import com.ait.tooling.server.core.pubsub.IPubSubHandlerRegistration;
import com.ait.tooling.server.core.pubsub.IPubSubMessageReceivedHandler;
import com.ait.tooling.server.core.pubsub.IPublishDescriptor;
import com.ait.tooling.server.core.pubsub.ISubscribeDescriptor;
import com.ait.tooling.server.core.pubsub.PubSubChannelType;
import com.ait.tooling.server.core.pubsub.PubSubException;
import com.ait.tooling.server.core.security.AnonOnlyAuthorizationProvider;
import com.ait.tooling.server.core.security.AuthorizationResult;
import com.ait.tooling.server.core.security.IAuthorizationProvider;
import com.ait.tooling.server.core.security.ICryptoProvider;

public class ServerContextInstance implements IServerContext
{
    private static final long                          serialVersionUID    = 8451400323005323866L;

    private static ApplicationContext                  APPLICATION_CONTEXT = null;

    private static final Logger                        logger              = Logger.getLogger(ServerContextInstance.class);

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
        if (getApplicationContext().containsBean("AuthorizationProvider"))
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
        if (getApplicationContext().containsBean("PrincipalsKeysProvider"))
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
    public final ICoreServerManager getCoreServerManager()
    {
        return Objects.requireNonNull(getBeanSafely("CoreServerManager", ICoreServerManager.class), "CoreServerManager is null, initialization error.");
    }

    @Override
    public final IExecutorServiceDescriptorProvider getExecutorServiceDescriptorProvider()
    {
        return Objects.requireNonNull(getBeanSafely("ExecutorServiceDescriptorProvider", IExecutorServiceDescriptorProvider.class), "ExecutorServiceDescriptorProvider is null, initialization error.");
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
    public final IPubSubDescriptorProvider getPubSubDescriptorProvider()
    {
        return Objects.requireNonNull(getBeanSafely("PubSubDescriptorProvider", IPubSubDescriptorProvider.class), "PubSubDescriptorProvider is null, initialization error.");
    }

    @Override
    public final JSONObject publish(String name, JSONObject message) throws Exception
    {
        name = Objects.requireNonNull(name);

        message = Objects.requireNonNull(message);

        final IPublishDescriptor desc = getPubSubDescriptorProvider().getPublishDescriptor(name);

        if (null != desc)
        {
            desc.publish(message);
        }
        else
        {
            throw new PubSubException("IPublishDescriptor " + name + " not found");
        }
        return message;
    }

    @Override
    public final JSONObject publish(String name, PubSubChannelType type, JSONObject message) throws Exception
    {
        name = Objects.requireNonNull(name);

        type = Objects.requireNonNull(type);

        message = Objects.requireNonNull(message);

        final IPublishDescriptor desc = getPubSubDescriptorProvider().getPublishDescriptor(name, type);

        if (null != desc)
        {
            desc.publish(message);
        }
        else
        {
            throw new PubSubException("IPublishDescriptor " + name + " type " + type.getValue() + " not found");
        }
        return message;
    }

    @Override
    public final JSONObject publish(String name, List<PubSubChannelType> list, JSONObject message) throws Exception
    {
        name = Objects.requireNonNull(name);

        list = Objects.requireNonNull(list);

        message = Objects.requireNonNull(message);

        final IPublishDescriptor desc = getPubSubDescriptorProvider().getPublishDescriptor(name, list);

        if (null != desc)
        {
            desc.publish(message);
        }
        else
        {
            StringBuilder builder = new StringBuilder();

            for (PubSubChannelType type : list)
            {
                builder.append(type.getValue());

                builder.append(",");
            }
            final int last = builder.lastIndexOf(",");

            if (last > 0)
            {
                builder.deleteCharAt(last);
            }
            throw new PubSubException("IPublishDescriptor " + name + " of types [" + builder.toString() + "] not found");
        }
        return message;
    }

    @Override
    public final IPubSubHandlerRegistration addMessageReceivedHandler(String name, IPubSubMessageReceivedHandler handler) throws Exception
    {
        name = Objects.requireNonNull(name);

        handler = Objects.requireNonNull(handler);

        final ISubscribeDescriptor desc = getPubSubDescriptorProvider().getSubscribeDescriptor(name);

        if (null != desc)
        {
            return desc.addMessageReceivedHandler(handler);
        }
        else
        {
            throw new PubSubException("ISubscribeDescriptor " + name + " not found");
        }
    }

    @Override
    public final IPubSubHandlerRegistration addMessageReceivedHandler(String name, PubSubChannelType type, IPubSubMessageReceivedHandler handler) throws Exception
    {
        name = Objects.requireNonNull(name);

        type = Objects.requireNonNull(type);

        handler = Objects.requireNonNull(handler);

        final ISubscribeDescriptor desc = getPubSubDescriptorProvider().getSubscribeDescriptor(name, type);

        if (null != desc)
        {
            return desc.addMessageReceivedHandler(handler);
        }
        else
        {
            throw new PubSubException("ISubscribeDescriptor " + name + " type " + type.getValue() + " not found");
        }
    }

    @Override
    public final IPubSubHandlerRegistration addMessageReceivedHandler(String name, List<PubSubChannelType> list, IPubSubMessageReceivedHandler handler) throws Exception
    {
        name = Objects.requireNonNull(name);

        list = Objects.requireNonNull(list);

        handler = Objects.requireNonNull(handler);

        final ISubscribeDescriptor desc = getPubSubDescriptorProvider().getSubscribeDescriptor(name, list);

        if (null != desc)
        {
            return desc.addMessageReceivedHandler(handler);
        }
        else
        {
            StringBuilder builder = new StringBuilder();

            for (PubSubChannelType type : list)
            {
                builder.append(type.getValue());

                builder.append(",");
            }
            final int last = builder.lastIndexOf(",");

            if (last > 0)
            {
                builder.deleteCharAt(last);
            }
            throw new PubSubException("ISubscribeDescriptor " + name + " of types [" + builder.toString() + "] not found");
        }
    }

    @Override
    public final JSONObject json()
    {
        return new JSONObject();
    }

    @Override
    public final JSONObject json(final Map<String, ?> valu)
    {
        return new JSONObject(Objects.requireNonNull(valu));
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
        return UUID.randomUUID().toString();
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
}
