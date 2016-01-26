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

package com.ait.tooling.server.core.support

import groovy.transform.CompileStatic
import groovy.transform.Memoized

import org.apache.log4j.Logger
import org.springframework.context.ApplicationContext
import org.springframework.core.env.Environment
import org.springframework.integration.channel.PublishSubscribeChannel
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.PollableChannel
import org.springframework.messaging.SubscribableChannel

import com.ait.tooling.common.api.java.util.StringOps
import com.ait.tooling.server.core.jmx.management.ICoreServerManager
import com.ait.tooling.server.core.json.JSONArray
import com.ait.tooling.server.core.json.JSONObject
import com.ait.tooling.server.core.json.binder.JSONBinder
import com.ait.tooling.server.core.json.parser.JSONParserException
import com.ait.tooling.server.core.json.schema.JSONSchema
import com.ait.tooling.server.core.pubsub.JSONMessageBuilder
import com.ait.tooling.server.core.security.AuthorizationResult
import com.ait.tooling.server.core.security.IAuthorizationProvider
import com.ait.tooling.server.core.security.ICryptoProvider
import com.ait.tooling.server.core.security.ISignatoryProvider
import com.ait.tooling.server.core.security.session.IServerSessionRepository
import com.ait.tooling.server.core.security.session.IServerSessionRepositoryProvider
import com.ait.tooling.server.core.support.instrument.telemetry.ITelemetryProvider
import com.ait.tooling.server.core.support.spring.IBuildDescriptorProvider
import com.ait.tooling.server.core.support.spring.IPropertiesResolver
import com.ait.tooling.server.core.support.spring.IServerContext
import com.ait.tooling.server.core.support.spring.ServerContextInstance
import com.ait.tooling.server.core.support.spring.network.ICoreNetworkProvider

@CompileStatic
public class CoreGroovySupport implements IServerContext, Closeable
{
    private static final CoreGroovySupport  INSTANCE = new CoreGroovySupport()

    private final Logger                    m_logger = Logger.getLogger(getClass())

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
    public List<String> getPrincipalsKeys()
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

    @Memoized
    public IServerSessionRepositoryProvider getServerSessionRepositoryProvider()
    {
        getServerContext().getServerSessionRepositoryProvider()
    }

    @Memoized
    public IServerSessionRepository getServerSessionRepository(String domain)
    {
        getServerSessionRepositoryProvider().getServerSessionRepository(Objects.requireNonNull(domain))
    }

    @Override
    public AuthorizationResult isAuthorized(Object target, List<String> roles)
    {
        getServerContext().isAuthorized(target, roles)
    }

    @Memoized
    public ICryptoProvider getCryptoProvider()
    {
        getServerContext().getCryptoProvider()
    }

    @Memoized
    public ISignatoryProvider getSignatoryProvider()
    {
        getServerContext().getSignatoryProvider()
    }

    @Memoized
    public ICoreNetworkProvider getCoreNetworkProvider()
    {
        getServerContext().getCoreNetworkProvider()
    }

    @Memoized
    public MessageChannel getMessageChannel(String name)
    {
        getServerContext().getMessageChannel(Objects.requireNonNull(name))
    }

    @Memoized
    public PublishSubscribeChannel getPublishSubscribeChannel(String name)
    {
        getServerContext().getPublishSubscribeChannel(Objects.requireNonNull(name))
    }

    @Memoized
    public SubscribableChannel getSubscribableChannel(String name)
    {
        getServerContext().getSubscribableChannel(Objects.requireNonNull(name))
    }

    @Memoized
    public PollableChannel getPollableChannel(String name)
    {
        getServerContext().getPollableChannel(Objects.requireNonNull(name))
    }

    @Override
    public boolean publish(String name, JSONObject message)
    {
        publish(Objects.requireNonNull(name), JSONMessageBuilder.createMessage(Objects.requireNonNull(message)))
    }

    @Override
    public boolean publish(String name, JSONObject message, long timeout)
    {
        publish(Objects.requireNonNull(name), JSONMessageBuilder.createMessage(Objects.requireNonNull(message)), timeout)
    }

    @Override
    public boolean publish(String name, JSONObject message, Map<String, ?> headers)
    {
        publish(Objects.requireNonNull(name), JSONMessageBuilder.createMessage(Objects.requireNonNull(message), Objects.requireNonNull(headers)))
    }

    @Override
    public boolean publish(String name, JSONObject message, Map<String, ?> headers, long timeout)
    {
        publish(Objects.requireNonNull(name), JSONMessageBuilder.createMessage(Objects.requireNonNull(message), Objects.requireNonNull(headers)), timeout)
    }

    @Override
    public <T> boolean publish(String name, Message<T> message)
    {
        def channel = getMessageChannel(Objects.requireNonNull(name))

        if (channel)
        {
            return channel.send(Objects.requireNonNull(message))
        }
        throw new IllegalArgumentException("MessageChannel ${name} does not exist.")
    }

    @Override
    public <T> boolean publish(String name, Message<T> message, long timeout)
    {
        def channel = getMessageChannel(Objects.requireNonNull(name))

        if (channel)
        {
            return channel.send(Objects.requireNonNull(message), timeout)
        }
        throw new IllegalArgumentException("MessageChannel ${name} does not exist.")
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
    public JSONObject jsonParse(Reader reader) throws JSONParserException
    {
        getServerContext().jsonParse(Objects.requireNonNull(reader))
    }

    @Override
    public JSONObject jsonParse(InputStream stream) throws JSONParserException
    {
        getServerContext().jsonParse(Objects.requireNonNull(stream))
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

    @Override
    public JSONBinder binder()
    {
        getServerContext().binder()
    }

    @Memoized
    public ITelemetryProvider getTelemetryProvider()
    {
        getServerContext().getTelemetryProvider()
    }

    @Override
    public boolean telemetry(String category, Object message)
    {
        def provider = getTelemetryProvider()

        if (provider.isActive())
        {
            return provider.broadcast(Objects.requireNonNull(category), Objects.requireNonNull(message))
        }
        false
    }

    @Override
    public boolean telemetry(String category, List<String> tags, Object message)
    {
        def provider = getTelemetryProvider()

        if (provider.isActive())
        {
            return provider.broadcast(Objects.requireNonNull(category), Objects.requireNonNull(tags), Objects.requireNonNull(message))
        }
        false
    }

    @Override
    public String toTrimOrNull(String string)
    {
        StringOps.toTrimOrNull(string)
    }

    @Override
    public String toTrimOrElse(String string, String otherwise)
    {
        StringOps.toTrimOrElse(string, otherwise)
    }
}
