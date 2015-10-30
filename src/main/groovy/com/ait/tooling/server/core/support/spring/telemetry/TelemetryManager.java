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
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.pubsub.JSONMessageBuilder;

public class TelemetryManager implements ITelemetryManager, BeanFactoryAware
{
    private static final long  serialVersionUID = 8200974186104113295L;

    private boolean            m_is_sending;

    private MessageChannel     m_tloutbound;

    private ITelemetryProvider m_tlprovider;

    public TelemetryManager()
    {
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
    public void close() throws IOException
    {
        m_is_sending = false;
    }

    private JSONObject getHeartbeatTelemetryObject()
    {
        return Builder.makeCoreTelemetryObject("heartbeat", new JSONObject());
    }

    @Override
    public void heartbeat()
    {
        if (isSending())
        {
            if (null != m_tlprovider)
            {
                m_tlprovider.post(JSONMessageBuilder.createMessage(getHeartbeatTelemetryObject()));
            }
        }
    }

    @Override
    public void setBeanFactory(final BeanFactory factory) throws BeansException
    {
        if (factory instanceof DefaultListableBeanFactory)
        {
            m_tlprovider = Objects.requireNonNull(((DefaultListableBeanFactory) factory).getBean("TelemetryProvider", ITelemetryProvider.class));

            m_tloutbound = Objects.requireNonNull(((DefaultListableBeanFactory) factory).getBean("CoreTelemetryOutboundChannel", MessageChannel.class));

            PublishSubscribeChannel channel = Objects.requireNonNull(m_tlprovider.getPublishSubscribeChannel());

            setSending(true);

            channel.subscribe(new MessageHandler()
            {
                @Override
                @SuppressWarnings("unchecked")
                public void handleMessage(Message<?> message) throws MessagingException
                {
                    if (isSending())
                    {
                        if (null != message)
                        {
                            if (message.getPayload() instanceof JSONObject)
                            {
                                final Message<JSONObject> publish = Builder.doEnsureTelemetryMessageForPublish((Message<JSONObject>) message, m_tlprovider.getProductBuildDescriptor());

                                if (null != publish)
                                {
                                    m_tloutbound.send(publish);
                                }
                            }
                        }
                    }
                }
            });
        }
    }
}
