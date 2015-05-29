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
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.ait.tooling.json.JSONObject;
import com.ait.tooling.json.parser.JSONParser;
import com.ait.tooling.json.parser.JSONParserException;

@SuppressWarnings("serial")
public class AbstractJmsSubscribeDescriptor implements MessageListener, ISubscribeDescriptor
{
    private final String                     m_name;

    private final PubSubChannelType          m_type;

    private final SubscribeDescriptorSupport m_supp   = new SubscribeDescriptorSupport();

    private final Logger                     m_logger = Logger.getLogger(getClass());

    protected AbstractJmsSubscribeDescriptor(final String name, final PubSubChannelType type)
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
    public void onMessage(final Message message)
    {
        Objects.requireNonNull(message);

        if (message instanceof TextMessage)
        {
            try
            {
                final Object result = new JSONParser().parse(((TextMessage) message).getText());

                if (result instanceof JSONObject)
                {
                    final JSONObject object = ((JSONObject) result);

                    logger().info("Got message and dispatch " + object.toJSONString());

                    m_supp.dispatch(new MessageReceivedEvent(this, object));
                }
                else
                {
                    logger().error("Message was not JSONObject");

                    throw new RuntimeException("Message was not JSONObject");
                }
            }
            catch (JSONParserException e)
            {
                logger().error("JSONParserException", e);

                throw new RuntimeException(e);
            }
            catch (JMSException e)
            {
                logger().error("JMSException", e);

                throw new RuntimeException(e);
            }
            catch (Exception e)
            {
                logger().error("Dispatch Error", e);

                throw new RuntimeException(e);
            }
        }
        else
        {
            logger().error("Message must be of type TextMessage");

            throw new IllegalArgumentException("Message must be of type TextMessage");
        }
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
    public IPubSubHandlerRegistration addMessageReceivedHandler(final IPubSubMessageReceivedHandler handler)
    {
        return m_supp.addMessageReceivedHandler(Objects.requireNonNull(handler));
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
