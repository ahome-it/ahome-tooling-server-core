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

import groovy.lang.Closure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import com.ait.tooling.common.api.java.util.UUID;
import com.ait.tooling.json.JSONObject;
import com.ait.tooling.json.schema.JSONSchema;
import com.ait.tooling.server.core.jmx.management.ICoreServerManager;
import com.ait.tooling.server.core.pubsub.IPubSubDescriptor;
import com.ait.tooling.server.core.pubsub.IPubSubDescriptorProvider;
import com.ait.tooling.server.core.pubsub.IPubSubHandlerRegistration;
import com.ait.tooling.server.core.pubsub.IPubSubMessageReceivedHandler;
import com.ait.tooling.server.core.pubsub.IPubSubStateChangedHandler;
import com.ait.tooling.server.core.pubsub.MessageReceivedEvent;
import com.ait.tooling.server.core.pubsub.PubSubChannelType;
import com.ait.tooling.server.core.pubsub.PubSubException;
import com.ait.tooling.server.core.pubsub.PubSubNextEventActionType;
import com.ait.tooling.server.core.pubsub.PubSubStateType;
import com.ait.tooling.server.core.pubsub.StateChangedEvent;
import com.ait.tooling.server.core.security.AnonOnlyAuthorizationProvider;
import com.ait.tooling.server.core.security.AuthorizationResult;
import com.ait.tooling.server.core.security.IAuthorizationProvider;

public final class ServerContextInstance implements IServerContext
{
    private static final long                                         serialVersionUID = 8451400323005323866L;

    private static final Logger                                       logger           = Logger.getLogger(ServerContextInstance.class);

    private final static AnonOnlyAuthorizationProvider                DEFAULT_AUTH     = new AnonOnlyAuthorizationProvider();

    private final static DefaultPrincipalsKeysProvider                DEFAULT_KEYS     = new DefaultPrincipalsKeysProvider();

    private final static ServerContextInstance                        INSTANCE         = new ServerContextInstance();

    private final static ConcurrentHashMap<String, IPubSubDescriptor> PUBSUB_MAP       = new ConcurrentHashMap<String, IPubSubDescriptor>();

    private ApplicationContext                                        m_context;

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

    public final void setApplicationContext(final ApplicationContext context)
    {
        m_context = Objects.requireNonNull(context);
    }

    @Override
    public final ApplicationContext getApplicationContext()
    {
        return m_context;
    }

    @Override
    public final Environment getEnvironment()
    {
        return m_context.getEnvironment();
    }

    @Override
    public final <B> B getBean(final String name, final Class<B> type)
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
    public final ICoreServerManager getCoreServerManager()
    {
        return getBean("CoreServerManager", ICoreServerManager.class);
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

    @Override
    public IPubSubDescriptorProvider getPubSubDescriptorProvider()
    {
        return getBean("PubSubDescriptorProvider", IPubSubDescriptorProvider.class);
    }

    @Override
    public JSONObject publish(String name, PubSubChannelType type, JSONObject message) throws Exception
    {
        type = Objects.requireNonNull(type);

        message = Objects.requireNonNull(message);

        IPubSubDescriptor desc = PUBSUB_MAP.get(Objects.requireNonNull(name));

        if (null != desc)
        {
            if (type == desc.getChannelType())
            {
                desc.publish(message);
            }
            else
            {
                throw new PubSubException("IPubSubDescriptor " + name + " wrong type " + type.getValue());
            }
        }
        else
        {
            desc = getServerContext().getPubSubDescriptorProvider().getPubSubDescriptor(name, type);

            if (null != desc)
            {
                PUBSUB_MAP.put(name, desc);

                desc.publish(message);
            }
            else
            {
                throw new PubSubException("IPubSubDescriptor " + name + " type " + type.getValue() + " not found");
            }
        }
        return message;
    }

    @Override
    public IPubSubHandlerRegistration addMessageReceivedHandler(String name, PubSubChannelType type, Closure<JSONObject> handler) throws Exception
    {
        return addMessageReceivedHandler(Objects.requireNonNull(name), Objects.requireNonNull(type), new OnMessage(Objects.requireNonNull(handler)));
    }

    @Override
    public IPubSubHandlerRegistration addMessageReceivedHandler(String name, PubSubChannelType type, IPubSubMessageReceivedHandler handler) throws Exception
    {
        type = Objects.requireNonNull(type);

        handler = Objects.requireNonNull(handler);

        IPubSubDescriptor desc = PUBSUB_MAP.get(Objects.requireNonNull(name));

        if (null != desc)
        {
            if (type == desc.getChannelType())
            {
                return desc.addMessageReceivedHandler(handler);
            }
            else
            {
                throw new PubSubException("IPubSubDescriptor " + name + " wrong type " + type.getValue());
            }
        }
        else
        {
            desc = getServerContext().getPubSubDescriptorProvider().getPubSubDescriptor(name, type);

            if (null != desc)
            {
                PUBSUB_MAP.put(name, desc);

                return desc.addMessageReceivedHandler(handler);
            }
            else
            {
                throw new PubSubException("IPubSubDescriptor " + name + " type " + type.getValue() + " not found");
            }
        }
    }

    @Override
    public IPubSubHandlerRegistration addStateChangedHandler(String name, PubSubChannelType type, IPubSubStateChangedHandler handler) throws Exception
    {
        type = Objects.requireNonNull(type);

        handler = Objects.requireNonNull(handler);

        IPubSubDescriptor desc = PUBSUB_MAP.get(Objects.requireNonNull(name));

        if (null != desc)
        {
            if (type == desc.getChannelType())
            {
                return desc.addStateChangedHandler(handler);
            }
            else
            {
                throw new PubSubException("IPubSubDescriptor " + name + " wrong type " + type.getValue());
            }
        }
        else
        {
            desc = getServerContext().getPubSubDescriptorProvider().getPubSubDescriptor(name, type);

            if (null != desc)
            {
                PUBSUB_MAP.put(name, desc);

                return desc.addStateChangedHandler(handler);
            }
            else
            {
                throw new PubSubException("IPubSubDescriptor " + name + " type " + type.getValue() + " not found");
            }
        }
    }

    @Override
    public IPubSubHandlerRegistration addStateChangedHandler(String name, PubSubChannelType type, Closure<PubSubStateType> handler) throws Exception
    {
        return addStateChangedHandler(Objects.requireNonNull(name), Objects.requireNonNull(type), new OnStateChanged(Objects.requireNonNull(handler)));
    }

    private static final class OnMessage implements IPubSubMessageReceivedHandler
    {
        private final Closure<JSONObject> m_clos;

        public OnMessage(final Closure<JSONObject> clos)
        {
            m_clos = Objects.requireNonNull(clos);
        }

        @Override
        public PubSubNextEventActionType onMesageReceived(final MessageReceivedEvent event)
        {
            m_clos.call(Objects.requireNonNull(Objects.requireNonNull(event).getValue()));

            return PubSubNextEventActionType.CONTINUE;
        }
    }

    private static final class OnStateChanged implements IPubSubStateChangedHandler
    {
        private final Closure<PubSubStateType> m_clos;

        public OnStateChanged(final Closure<PubSubStateType> clos)
        {
            m_clos = Objects.requireNonNull(clos);
        }

        @Override
        public PubSubNextEventActionType onStateChanged(final StateChangedEvent event)
        {
            m_clos.call(Objects.requireNonNull(Objects.requireNonNull(event).getValue()));

            return PubSubNextEventActionType.CONTINUE;
        }
    }

    @Override
    public JSONObject json()
    {
        return new JSONObject();
    }

    @Override
    public JSONObject json(final Map<String, ?> valu)
    {
        return new JSONObject(Objects.requireNonNull(valu));
    }

    @Override
    public JSONObject json(final String name, final Object value)
    {
        return new JSONObject(Objects.requireNonNull(name), value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject json(Collection<?> collection)
    {
        collection = Objects.requireNonNull(collection);

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
    public JSONObject json(final List<?> list)
    {
        return new JSONObject(Objects.requireNonNull(list));
    }

    @Override
    public String uuid()
    {
        return UUID.uuid();
    }

    @Override
    public JSONSchema jsonschema(final Map<String, ?> schema)
    {
        return JSONSchema.cast(json(schema));
    }

    @Override
    public Logger logger()
    {
        return logger;
    }
}
