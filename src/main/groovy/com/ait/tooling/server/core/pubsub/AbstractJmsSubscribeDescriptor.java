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
    protected AbstractJmsSubscribeDescriptor(final String name)
    {
        super(Objects.requireNonNull(name));
    }

    @Override
    public void onMessage(final Message text)
    {
        if (text instanceof TextMessage)
        {
            try
            {
                final Object result = new JSONParser().parse(((TextMessage) text).getText());

                if (result instanceof JSONObject)
                {
                    final JSONMessage message = new JSONMessage((JSONObject) result);

                    for (IMessageReceivedHandler handler : getMessageReceivedHandlers())
                    {
                        try
                        {
                            handler.onMessageReceived(message);
                        }
                        catch (Exception e)
                        {
                            logger().error("onMessageReceived() error", e);
                        }
                    }
                    getSubscribeDescriptorSupport().dispatch(message);
                }
                else
                {
                    logger().error("Message was not a JSONObject");
                }
            }
            catch (JSONParserException e)
            {
                logger().error("JSONParserException", e);
            }
            catch (JMSException e)
            {
                logger().error("JMSException", e);
            }
            catch (Exception e)
            {
                logger().error("Dispatch Error", e);
            }
        }
        else
        {
            logger().error("Message must be of type TextMessage");
        }
    }
}
