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

package com.ait.tooling.server.core.support.spring.telemetry;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.Message;

import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.pubsub.JSONMessageBuilder;
import com.ait.tooling.server.core.support.spring.IBuildDescriptor;

public class TelemetryProvider implements ITelemetryProvider, BeanFactoryAware
{
    private static final long       serialVersionUID = -5006001627498494810L;

    private boolean                 m_is_sending;

    private IBuildDescriptor        m_descriptor;

    private PublishSubscribeChannel m_telepubsub;

    public TelemetryProvider(final IBuildDescriptor descriptor)
    {
        m_descriptor = Objects.requireNonNull(descriptor);
    }

    @Override
    public void setSending(final boolean sending)
    {
        m_is_sending = sending;
    }

    @Override
    public boolean isSending()
    {
        return m_is_sending;
    }

    @Override
    public IBuildDescriptor getProductBuildDescriptor()
    {
        return m_descriptor;
    }

    @Override
    public PublishSubscribeChannel getPublishSubscribeChannel()
    {
        return m_telepubsub;
    }

    @Override
    public void close() throws IOException
    {
        m_is_sending = false;
    }

    @Override
    public Message<JSONObject> make(final String action, final JSONObject message)
    {
        return JSONMessageBuilder.createMessage(Builder.makeCoreTelemetryObject(action, message));
    }

    @Override
    public boolean post(final Message<JSONObject> message)
    {
        if (isSending())
        {
            return getPublishSubscribeChannel().send(Objects.requireNonNull(message));
        }
        return false;
    }

    @Override
    public void setBeanFactory(final BeanFactory factory) throws BeansException
    {
        if (factory instanceof DefaultListableBeanFactory)
        {
            m_telepubsub = Objects.requireNonNull(((DefaultListableBeanFactory) factory).getBean("CoreTelemetryPublishSubscribeChannel", PublishSubscribeChannel.class));

            setSending(true);
        }
    }
}
