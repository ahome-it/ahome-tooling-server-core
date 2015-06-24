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

import org.springframework.context.ApplicationContext
import org.springframework.core.env.Environment
import org.springframework.integration.channel.PublishSubscribeChannel
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.PollableChannel
import org.springframework.messaging.SubscribableChannel

import com.ait.tooling.json.JSONArray
import com.ait.tooling.json.JSONObject
import com.ait.tooling.json.parser.JSONParserException
import com.ait.tooling.json.schema.JSONSchema
import com.ait.tooling.server.core.jmx.management.ICoreServerManager
import com.ait.tooling.server.core.security.AuthorizationResult
import com.ait.tooling.server.core.security.IAuthorizationProvider
import com.ait.tooling.server.core.security.ICryptoProvider
import com.ait.tooling.server.core.security.ISignatoryProvider
import com.ait.tooling.server.core.security.session.IServerSessionRepository
import com.ait.tooling.server.core.security.session.IServerSessionRepositoryProvider
import com.ait.tooling.server.core.support.spring.IBuildDescriptorProvider
import com.ait.tooling.server.core.support.spring.IPropertiesResolver
import com.ait.tooling.server.core.support.spring.IServerContext
import com.ait.tooling.server.core.support.spring.ServerContextInstance

@CompileStatic
public trait CoreGroovyTrait
{
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
    public IServerSessionRepository getServerSessionRepository(String domain_name)
    {
        getServerSessionRepositoryProvider().getServerSessionRepository(Objects.requireNonNull(domain_name))
    }

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

    public <T> boolean publish(String name, Message<T> message)
    {
        Objects.requireNonNull(message)

        MessageChannel channel = getMessageChannel(Objects.requireNonNull(name))

        if (channel)
        {
            return channel.send(message)
        }
        throw new IllegalArgumentException("MessageChannel ${name} does not exist.")
    }

    public <T> boolean publish(String name, Message<T> message, long timeout)
    {
        Objects.requireNonNull(message)

        MessageChannel channel = getMessageChannel(Objects.requireNonNull(name))

        if (channel)
        {
            return channel.send(message, timeout)
        }
        throw new IllegalArgumentException("MessageChannel ${name} does not exist.")
    }

    public boolean containsBean(String name)
    {
        getServerContext().containsBean(Objects.requireNonNull(name))
    }

    public <B> B getBean(String name, Class<B> type) throws Exception
    {
        getServerContext().getBean(Objects.requireNonNull(name), Objects.requireNonNull(type))
    }

    public <B> B getBeanSafely(String name, Class<B> type)
    {
        getServerContext().getBeanSafely(Objects.requireNonNull(name), Objects.requireNonNull(type))
    }

    public JSONObject json()
    {
        getServerContext().json()
    }

    public JSONObject json(Map<String, ?> map)
    {
        getServerContext().json(Objects.requireNonNull(map))
    }

    public JSONObject json(String name, Object value)
    {
        getServerContext().json(Objects.requireNonNull(name), value)
    }

    public JSONObject json(Collection<?> collection)
    {
        getServerContext().json(Objects.requireNonNull(collection))
    }

    public JSONObject json(List<?> list)
    {
        getServerContext().json(Objects.requireNonNull(list))
    }

    public JSONSchema jsonSchema(Map<String, ?> schema)
    {
        getServerContext().jsonSchema(Objects.requireNonNull(schema))
    }

    public String uuid()
    {
        getServerContext().uuid()
    }

    public JSONObject jsonParse(String string) throws JSONParserException
    {
        getServerContext().jsonParse(Objects.requireNonNull(string))
    }

    public JSONObject jsonParse(Reader reader) throws IOException, JSONParserException
    {
        getServerContext().jsonParse(Objects.requireNonNull(reader))
    }

    public JSONArray jarr()
    {
        getServerContext().jarr()
    }

    public JSONArray jarr(JSONObject object)
    {
        getServerContext().jarr(Objects.requireNonNull(object))
    }

    public JSONArray jarr(List<?> list)
    {
        getServerContext().jarr(Objects.requireNonNull(list))
    }

    public JSONArray jarr(Map<String, ?> map)
    {
        getServerContext().jarr(Objects.requireNonNull(map))
    }

    public JSONArray jarr(String name, Object value)
    {
        getServerContext().jarr(Objects.requireNonNull(name), value)
    }

    public JSONArray jarr(Collection<?> collection)
    {
        getServerContext().jarr(Objects.requireNonNull(collection))
    }
}
