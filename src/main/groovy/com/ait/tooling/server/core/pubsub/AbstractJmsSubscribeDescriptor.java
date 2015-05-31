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

import java.util.Objects;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.ait.tooling.json.JSONObject;
import com.ait.tooling.json.parser.JSONParser;
import com.ait.tooling.json.parser.JSONParserException;

@SuppressWarnings("serial")
public abstract class AbstractJmsSubscribeDescriptor extends AbstractSubscribeDescriptor implements MessageListener
{
    protected AbstractJmsSubscribeDescriptor(final String name, final PubSubChannelType type)
    {
        super(Objects.requireNonNull(name), Objects.requireNonNull(type));

        if ((type != PubSubChannelType.QUEUE) && (type != PubSubChannelType.TOPIC))
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
                    final MessageReceivedEvent event = new MessageReceivedEvent(this, ((JSONObject) result));

                    for (IPubSubMessageReceivedHandler listener : getMessageReceivedHandlers())
                    {
                        final PubSubNextEventActionType next = listener.onMessageReceived(event);

                        if (PubSubNextEventActionType.CANCEL == next)
                        {
                            event.cancel();
                        }
                        else if (PubSubNextEventActionType.REDISPATCH == next)
                        {
                            final IPublishDescriptor pubs = getPublishDescriptor();

                            if (null != pubs)
                            {
                                try
                                {
                                    pubs.publish(event.getMessage().getPayload());
                                }
                                catch (Exception e)
                                {
                                    logger().error("REDISPATCH threw Exception, cancelling event.", e);
                                }
                            }
                            else
                            {
                                logger().error("REDISPATCH didnt find IPublishDescriptor, cancelling event.");
                            }
                            event.cancel();
                        }
                        if (event.isCancelled())
                        {
                            return;
                        }
                    }
                    getSubscribeDescriptorSupport().dispatch(event, this);
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
}
