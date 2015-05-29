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

import com.ait.tooling.json.JSONObject
import com.ait.tooling.json.parser.JSONParserException
import com.ait.tooling.json.schema.JSONSchema
import com.ait.tooling.server.core.jmx.management.ICoreServerManager
import com.ait.tooling.server.core.pubsub.IPubSubDescriptorProvider
import com.ait.tooling.server.core.pubsub.IPubSubHandlerRegistration
import com.ait.tooling.server.core.pubsub.IPubSubMessageReceivedHandler
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
    public IPubSubDescriptorProvider getPubSubDescriptorProvider()
    {
        getServerContext().getPubSubDescriptorProvider()
    }

    @Memoized
    public ICryptoProvider getCryptoProvider()
    {
        getServerContext().getCryptoProvider()
    }

    @Override
    public <B> B getBean(String name, Class<B> type)
    {
        getApplicationContext().getBean(Objects.requireNonNull(name), Objects.requireNonNull(type))
    }

    @Override
    public JSONObject publish(String name, PubSubChannelType type, JSONObject message) throws Exception
    {
        getServerContext().publish(Objects.requireNonNull(name), Objects.requireNonNull(type), Objects.requireNonNull(message))
    }

    @Override
    public IPubSubHandlerRegistration addMessageReceivedHandler(String name, PubSubChannelType type, Closure<JSONObject> handler) throws Exception
    {
        Objects.requireNonNull(handler)

        getServerContext().addMessageReceivedHandler(Objects.requireNonNull(name), Objects.requireNonNull(type), handler)
    }

    @Override
    public IPubSubHandlerRegistration addMessageReceivedHandler(String name, PubSubChannelType type, IPubSubMessageReceivedHandler handler) throws Exception
    {
        getServerContext().addMessageReceivedHandler(Objects.requireNonNull(name), Objects.requireNonNull(type), Objects.requireNonNull(handler))
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
    public JSONObject publish(String name, JSONObject message) throws Exception
    {
        getServerContext().publish(Objects.requireNonNull(name), Objects.requireNonNull(message))
    }

    @Override
    public JSONObject publish(String name, List<PubSubChannelType> list, JSONObject message) throws Exception
    {
        getServerContext().publish(Objects.requireNonNull(name), Objects.requireNonNull(list),  Objects.requireNonNull(message))
    }

    @Override
    public IPubSubHandlerRegistration addMessageReceivedHandler(String name, Closure<JSONObject> handler) throws Exception
    {
        Objects.requireNonNull(handler)

        getServerContext().addMessageReceivedHandler(Objects.requireNonNull(name), handler)
    }

    @Override
    public IPubSubHandlerRegistration addMessageReceivedHandler(String name, List<PubSubChannelType> list, Closure<JSONObject> handler) throws Exception
    {
        Objects.requireNonNull(handler)

        getServerContext().addMessageReceivedHandler(Objects.requireNonNull(name), Objects.requireNonNull(list), handler)
    }

    @Override
    public IPubSubHandlerRegistration addMessageReceivedHandler(String name, IPubSubMessageReceivedHandler handler) throws Exception
    {
        getServerContext().addMessageReceivedHandler(Objects.requireNonNull(name), Objects.requireNonNull(handler))
    }

    @Override
    public IPubSubHandlerRegistration addMessageReceivedHandler(String name, List<PubSubChannelType> list, IPubSubMessageReceivedHandler handler) throws Exception
    {
        getServerContext().addMessageReceivedHandler(Objects.requireNonNull(name), Objects.requireNonNull(list), Objects.requireNonNull(handler))
    }
}
