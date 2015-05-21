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

package com.ait.tooling.server.core.support

import groovy.transform.CompileStatic
import groovy.transform.Memoized

import org.apache.log4j.Logger
import org.springframework.core.env.Environment
import org.springframework.web.context.WebApplicationContext

import com.ait.tooling.json.JSONObject
import com.ait.tooling.json.schema.JSONSchema
import com.ait.tooling.server.core.jmx.management.ICoreServerManager
import com.ait.tooling.server.core.pubsub.IPubSubDescriptorProvider
import com.ait.tooling.server.core.pubsub.IPubSubHandlerRegistration
import com.ait.tooling.server.core.pubsub.IPubSubMessageReceivedHandler
import com.ait.tooling.server.core.pubsub.IPubSubStateChangedHandler
import com.ait.tooling.server.core.pubsub.PubSubChannelType
import com.ait.tooling.server.core.pubsub.PubSubStateType
import com.ait.tooling.server.core.security.AuthorizationResult
import com.ait.tooling.server.core.security.IAuthorizationProvider
import com.ait.tooling.server.core.support.spring.IBuildDescriptorProvider
import com.ait.tooling.server.core.support.spring.IExecutorServiceDescriptorProvider
import com.ait.tooling.server.core.support.spring.IPropertiesResolver
import com.ait.tooling.server.core.support.spring.IServerContext
import com.ait.tooling.server.core.support.spring.ServerContextInstance

@CompileStatic
public class CoreGroovySupport implements IServerContext, Closeable, Serializable
{
    public static final CoreGroovySupport INSTANCE = new CoreGroovySupport()

    private static final long serialVersionUID = 6853938976110096947L

    private final Logger    m_logger = Logger.getLogger(getClass())

    @Memoized
    public static final CoreGroovySupport getCoreGroovySupport()
    {
        INSTANCE
    }

    public CoreGroovySupport()
    {
    }

    @Memoized
    public IServerContext getServerContext()
    {
        ServerContextInstance.get()
    }

    @Memoized
    public WebApplicationContext getApplicationContext()
    {
        getServerContext().getApplicationContext()
    }

    @Memoized
    public Environment getEnvironment()
    {
        getServerContext().getEnvironment()
    }

    @Memoized
    public Iterable<String> getPrincipalsKeys()
    {
        getServerContext().getPrincipalsKeys()
    }

    @Memoized
    public ICoreServerManager getCoreServerManager()
    {
        getServerContext().getCoreServerManager()
    }

    @Memoized
    public IBuildDescriptorProvider getBuildDescriptorProvider()
    {
        getServerContext().getBuildDescriptorProvider()
    }

    @Memoized
    public IPropertiesResolver getPropertiesResolver()
    {
        getServerContext().getPropertiesResolver()
    }

    @Memoized
    public String getPropertyByName(String name)
    {
        getServerContext().getPropertyByName(name)
    }

    @Memoized
    public String getPropertyByName(String name, String otherwise)
    {
        getServerContext().getPropertyByName(name, otherwise)
    }

    @Memoized
    public IAuthorizationProvider getAuthorizationProvider()
    {
        getServerContext().getAuthorizationProvider()
    }

    @Override
    public AuthorizationResult isAuthorized(Object target, JSONObject principals)
    {
        getServerContext().isAuthorized(target, principals)
    }

    @Memoized
    public IExecutorServiceDescriptorProvider getExecutorServiceDescriptorProvider()
    {
        getServerContext().getExecutorServiceDescriptorProvider()
    }

    @Memoized
    public IPubSubDescriptorProvider getPubSubDescriptorProvider()
    {
        getServerContext().getPubSubDescriptorProvider()
    }

    @Override
    public <B> B getBean(String name, Class<B> type)
    {
        getApplicationContext().getBean(Objects.requireNonNull(name), Objects.requireNonNull(type))
    }

    @Override
    public Logger logger()
    {
        m_logger
    }

    @Override
    public JSONObject publish(String name, PubSubChannelType type, JSONObject message) throws Exception
    {
        getServerContext().publish(name, type, message)
    }

    @Override
    public IPubSubHandlerRegistration addMessageReceivedHandler(String name, PubSubChannelType type, Closure<JSONObject> handler) throws Exception
    {
        getServerContext().addMessageReceivedHandler(name, type, handler)
    }

    @Override
    public IPubSubHandlerRegistration addMessageReceivedHandler(String name, PubSubChannelType type, IPubSubMessageReceivedHandler handler) throws Exception
    {
        getServerContext().addMessageReceivedHandler(name, type, handler)
    }

    @Override
    public IPubSubHandlerRegistration addStateChangedHandler(String name, PubSubChannelType type, IPubSubStateChangedHandler handler) throws Exception
    {
        getServerContext().addStateChangedHandler(name, type, handler)
    }

    @Override
    public IPubSubHandlerRegistration addStateChangedHandler(String name, PubSubChannelType type, Closure<PubSubStateType> handler) throws Exception
    {
        getServerContext().addStateChangedHandler(name, type, handler)
    }

    @Override
    public JSONObject json()
    {
        new JSONObject()
    }

    @Override
    public JSONObject json(Map<String, ?> valu)
    {
        new JSONObject(valu)
    }

    @Override
    public JSONObject json(String name, Object value)
    {
        new JSONObject(name, value)
    }

    @Override
    public JSONObject json(Collection<?> collection)
    {
        if (collection instanceof List)
        {
            return json((List<?>) collection)
        }
        else if(collection instanceof Map)
        {
            return json((Map<String, ?>) collection)
        }
        else
        {
            final List list = []

            list.addAll(collection)

            return json(list)
        }
    }

    @Override
    public JSONObject json(List<?> list)
    {
        new JSONObject(list)
    }

    public JSONSchema schema(Map<String, ?> schema)
    {
        JSONSchema.cast(json(schema))
    }

    @Override
    public void close() throws IOException
    {
    }
}
