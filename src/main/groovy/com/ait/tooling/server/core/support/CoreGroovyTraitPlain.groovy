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

import org.springframework.context.ApplicationContext
import org.springframework.core.env.Environment
import org.springframework.integration.channel.PublishSubscribeChannel
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.PollableChannel
import org.springframework.messaging.SubscribableChannel

import com.ait.tooling.server.core.jmx.management.ICoreServerManager
import com.ait.tooling.server.core.json.support.JSONTrait
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
public trait CoreGroovyTraitPlain implements JSONTrait
{
    public IServerContext getServerContext()
    {
        ServerContextInstance.getServerContextInstance().getServerContext()
    }

    public ApplicationContext getApplicationContext()
    {
        getServerContext().getApplicationContext()
    }

    public Environment getEnvironment()
    {
        getServerContext().getEnvironment()
    }

    public List<String> getPrincipalsKeys()
    {
        getServerContext().getPrincipalsKeys()
    }

    public ICoreServerManager getCoreServerManager()
    {
        getServerContext().getCoreServerManager()
    }

    public IBuildDescriptorProvider getBuildDescriptorProvider()
    {
        getServerContext().getBuildDescriptorProvider()
    }

    public IPropertiesResolver getPropertiesResolver()
    {
        getServerContext().getPropertiesResolver()
    }

    public String getPropertyByName(String name)
    {
        getServerContext().getPropertyByName(Objects.requireNonNull(name))
    }

    public String getPropertyByName(String name, String otherwise)
    {
        getServerContext().getPropertyByName(Objects.requireNonNull(name), otherwise)
    }

    public IAuthorizationProvider getAuthorizationProvider()
    {
        getServerContext().getAuthorizationProvider()
    }

    public IServerSessionRepositoryProvider getServerSessionRepositoryProvider()
    {
        getServerContext().getServerSessionRepositoryProvider()
    }

    public IServerSessionRepository getServerSessionRepository(String domain_name)
    {
        getServerSessionRepositoryProvider().getServerSessionRepository(Objects.requireNonNull(domain_name))
    }

    public AuthorizationResult isAuthorized(Object target, List<String> roles)
    {
        getServerContext().isAuthorized(target, roles)
    }

    public ICryptoProvider getCryptoProvider()
    {
        getServerContext().getCryptoProvider()
    }

    public ISignatoryProvider getSignatoryProvider()
    {
        getServerContext().getSignatoryProvider()
    }

    public MessageChannel getMessageChannel(String name)
    {
        getServerContext().getMessageChannel(Objects.requireNonNull(name))
    }

    public PublishSubscribeChannel getPublishSubscribeChannel(String name)
    {
        getServerContext().getPublishSubscribeChannel(Objects.requireNonNull(name))
    }

    public SubscribableChannel getSubscribableChannel(String name)
    {
        getServerContext().getSubscribableChannel(Objects.requireNonNull(name))
    }

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

    public String uuid()
    {
        getServerContext().uuid()
    }
}
