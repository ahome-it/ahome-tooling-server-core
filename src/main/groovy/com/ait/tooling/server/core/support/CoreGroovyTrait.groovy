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

import org.springframework.context.ApplicationContext
import org.springframework.core.env.Environment
import org.springframework.integration.channel.PublishSubscribeChannel
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.PollableChannel
import org.springframework.messaging.SubscribableChannel

import com.ait.tooling.common.api.java.util.StringOps
import com.ait.tooling.server.core.jmx.management.ICoreServerManager
import com.ait.tooling.server.core.json.JSONObject
import com.ait.tooling.server.core.json.support.JSONTrait
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
import com.ait.tooling.server.core.support.spring.network.ICoreNetworkProvider

@CompileStatic
public trait CoreGroovyTrait implements JSONTrait
{
    @Memoized
    public IServerContext getServerContext()
    {
        CoreGroovySupport.getCoreGroovySupport()
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

    public boolean publish(String name, JSONObject message)
    {
        publish(Objects.requireNonNull(name), JSONMessageBuilder.createMessage(Objects.requireNonNull(message)))
    }

    public boolean publish(String name, JSONObject message, long timeout)
    {
        publish(Objects.requireNonNull(name), JSONMessageBuilder.createMessage(Objects.requireNonNull(message)), timeout)
    }

    public boolean publish(String name, JSONObject message, Map<String, ?> headers)
    {
        publish(Objects.requireNonNull(name), JSONMessageBuilder.createMessage(Objects.requireNonNull(message), Objects.requireNonNull(headers)))
    }

    public boolean publish(String name, JSONObject message, Map<String, ?> headers, long timeout)
    {
        publish(Objects.requireNonNull(name), JSONMessageBuilder.createMessage(Objects.requireNonNull(message), Objects.requireNonNull(headers)), timeout)
    }

    public <T> boolean publish(String name, Message<T> message)
    {
        getServerContext().publish(Objects.requireNonNull(name), Objects.requireNonNull(message))
    }

    public <T> boolean publish(String name, Message<T> message, long timeout)
    {
        getServerContext().publish(Objects.requireNonNull(name), Objects.requireNonNull(message), timeout)
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

    public String uuid()
    {
        getServerContext().uuid()
    }

    @Memoized
    public ITelemetryProvider getTelemetryProvider()
    {
        getServerContext().getTelemetryProvider()
    }

    public boolean telemetry(String category, Object message)
    {
        getServerContext().telemetry(Objects.requireNonNull(category), Objects.requireNonNull(message))
    }

    public boolean telemetry(String category, List<String> tags, Object message)
    {
        getServerContext().telemetry(Objects.requireNonNull(category), Objects.requireNonNull(tags), Objects.requireNonNull(message))
    }

    public String toTrimOrNull(String string)
    {
        StringOps.toTrimOrNull(string)
    }

    public String toTrimOrElse(String string, String otherwise)
    {
        StringOps.toTrimOrElse(string, otherwise)
    }
}
