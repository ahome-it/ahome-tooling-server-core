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
import org.springframework.context.ApplicationContext
import org.springframework.core.env.Environment

import com.ait.tooling.json.JSONArray
import com.ait.tooling.json.JSONObject
import com.ait.tooling.json.parser.JSONParserException
import com.ait.tooling.json.schema.JSONSchema
import com.ait.tooling.server.core.jmx.management.ICoreServerManager
import com.ait.tooling.server.core.pubsub.IMessageReceivedHandler
import com.ait.tooling.server.core.pubsub.IMessageReceivedHandlerRegistration
import com.ait.tooling.server.core.pubsub.IPubSubDescriptorProvider
import com.ait.tooling.server.core.pubsub.JSONMessage
import com.ait.tooling.server.core.pubsub.PubSubChannelType
import com.ait.tooling.server.core.security.AuthorizationResult
import com.ait.tooling.server.core.security.IAuthorizationProvider
import com.ait.tooling.server.core.security.ICryptoProvider
import com.ait.tooling.server.core.support.spring.IBuildDescriptorProvider
import com.ait.tooling.server.core.support.spring.IExecutorServiceDescriptorProvider
import com.ait.tooling.server.core.support.spring.IPropertiesResolver
import com.ait.tooling.server.core.support.spring.IServerContext
import com.ait.tooling.server.core.support.spring.ServerContextInstance

@CompileStatic
public class CoreGroovySupport implements IServerContext, Closeable, Serializable
{
    private static final CoreGroovySupport INSTANCE = new CoreGroovySupport()

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

    @Override
    public Logger logger()
    {
        m_logger
    }

    @Memoized
    public IServerContext getServerContext()
    {
        ServerContextInstance.getServerContextInstance().getServerContext()
    }

    @Memoized
    public ApplicationContext getApplicationContext()
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
        getServerContext().getPropertyByName(Objects.requireNonNull(name))
    }

    @Memoized
    public String getPropertyByName(String name, String otherwise)
    {
        getServerContext().getPropertyByName(Objects.requireNonNull(name), otherwise)
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
    public ICryptoProvider getCryptoProvider()
    {
        getServerContext().getCryptoProvider()
    }

    @Memoized
    public IPubSubDescriptorProvider getPubSubDescriptorProvider()
    {
        getServerContext().getPubSubDescriptorProvider()
    }

    @Override
    public void publish(String name, JSONMessage message)
    {
        Objects.requireNonNull(message)

        getPubSubDescriptorProvider().getPublishDescriptor(Objects.requireNonNull(name)).publish(message)
    }

    @Override
    public void publish(String name, PubSubChannelType type, JSONMessage message)
    {
        Objects.requireNonNull(message)

        getPubSubDescriptorProvider().getPublishDescriptor(Objects.requireNonNull(name), Objects.requireNonNull(type)).publish(message)
    }

    @Override
    public void publish(String name, List<PubSubChannelType> list, JSONMessage message)
    {
        Objects.requireNonNull(message)

        getPubSubDescriptorProvider().getPublishDescriptor(Objects.requireNonNull(name), Objects.requireNonNull(list)).publish(message)
    }

    @Override
    public IMessageReceivedHandlerRegistration addMessageReceivedHandler(String name, IMessageReceivedHandler handler)
    {
        Objects.requireNonNull(handler)

        getPubSubDescriptorProvider().getSubscribeDescriptor(Objects.requireNonNull(name)).addMessageReceivedHandler(handler)
    }

    @Override
    public IMessageReceivedHandlerRegistration addMessageReceivedHandler(String name, PubSubChannelType type, IMessageReceivedHandler handler)
    {
        Objects.requireNonNull(handler)

        getPubSubDescriptorProvider().getSubscribeDescriptor(Objects.requireNonNull(name), Objects.requireNonNull(type)).addMessageReceivedHandler(handler)
    }

    @Override
    public IMessageReceivedHandlerRegistration addMessageReceivedHandler(String name, List<PubSubChannelType> list, IMessageReceivedHandler handler)
    {
        Objects.requireNonNull(handler)

        getPubSubDescriptorProvider().getSubscribeDescriptor(Objects.requireNonNull(name), Objects.requireNonNull(list)).addMessageReceivedHandler(handler)
    }

    @Override
    public boolean containsBean(String name)
    {
        getServerContext().containsBean(Objects.requireNonNull(name))
    }

    @Override
    public <B> B getBean(String name, Class<B> type) throws Exception
    {
        getServerContext().getBean(Objects.requireNonNull(name), Objects.requireNonNull(type))
    }

    @Override
    public <B> B getBeanSafely(String name, Class<B> type)
    {
        getServerContext().getBeanSafely(Objects.requireNonNull(name), Objects.requireNonNull(type))
    }

    @Override
    public JSONObject json()
    {
        getServerContext().json()
    }

    @Override
    public JSONObject json(Map<String, ?> map)
    {
        getServerContext().json(Objects.requireNonNull(map))
    }

    @Override
    public JSONObject json(String name, Object value)
    {
        getServerContext().json(Objects.requireNonNull(name), value)
    }

    @Override
    public JSONObject json(Collection<?> collection)
    {
        getServerContext().json(Objects.requireNonNull(collection))
    }

    @Override
    public JSONObject json(List<?> list)
    {
        getServerContext().json(Objects.requireNonNull(list))
    }

    @Override
    public JSONSchema jsonSchema(Map<String, ?> schema)
    {
        getServerContext().jsonSchema(Objects.requireNonNull(schema))
    }

    @Override
    public void close() throws IOException
    {
    }

    @Override
    public String uuid()
    {
        getServerContext().uuid()
    }

    @Override
    public JSONObject jsonParse(String string) throws JSONParserException
    {
        getServerContext().jsonParse(Objects.requireNonNull(string))
    }

    @Override
    public JSONObject jsonParse(Reader reader) throws IOException, JSONParserException
    {
        getServerContext().jsonParse(Objects.requireNonNull(reader))
    }

    @Override
    public JSONArray jarr()
    {
        getServerContext().jarr()
    }

    @Override
    public JSONArray jarr(JSONObject object)
    {
        getServerContext().jarr(Objects.requireNonNull(object))
    }

    @Override
    public JSONArray jarr(List<?> list)
    {
        getServerContext().jarr(Objects.requireNonNull(list))
    }

    @Override
    public JSONArray jarr(Map<String, ?> map)
    {
        getServerContext().jarr(Objects.requireNonNull(map))
    }

    @Override
    public JSONArray jarr(String name, Object value)
    {
        getServerContext().jarr(Objects.requireNonNull(name), value)
    }

    @Override
    public JSONArray jarr(Collection<?> collection)
    {
        getServerContext().jarr(Objects.requireNonNull(collection))
    }
}
