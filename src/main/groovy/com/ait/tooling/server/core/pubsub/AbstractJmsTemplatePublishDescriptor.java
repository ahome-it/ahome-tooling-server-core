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

package com.ait.tooling.server.core.pubsub;

import java.io.IOException;
import java.util.Objects;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.ait.tooling.json.JSONObject;

@SuppressWarnings("serial")
public class AbstractJmsTemplatePublishDescriptor implements IJmsTempletePublishDescriptor
{
    private final String            m_name;

    private final PubSubChannelType m_type;

    private final Logger            m_logger = Logger.getLogger(getClass());

    private JmsTemplate             m_template;

    protected AbstractJmsTemplatePublishDescriptor(final String name, final PubSubChannelType type)
    {
        m_name = Objects.requireNonNull(name);

        m_type = Objects.requireNonNull(type);

        if ((m_type != PubSubChannelType.QUEUE) && (m_type != PubSubChannelType.TOPIC))
        {
            logger().error("Invalid PubSubChannelType for JMS " + type.getValue());

            throw new IllegalArgumentException("Invalid PubSubChannelType for JMS " + type.getValue());
        }
    }

    @Override
    public JmsTemplate getJmsTemplate()
    {
        return m_template;
    }

    @Override
    public void setJmsTemplate(final JmsTemplate template)
    {
        m_template = Objects.requireNonNull(template);
    }

    @Override
    public PubSubChannelType getChannelType()
    {
        return m_type;
    }

    @Override
    public String getName()
    {
        return m_name;
    }

    @Override
    public JSONObject publish(final JSONObject message) throws Exception
    {
        Objects.requireNonNull(message);

        getJmsTemplate().send(new MessageCreator()
        {
            public Message createMessage(final Session session) throws JMSException
            {
                return session.createTextMessage(message.toJSONString());
            }
        });
        return message;
    }

    @Override
    public void close() throws IOException
    {
    }

    public final Logger logger()
    {
        return m_logger;
    }
}
