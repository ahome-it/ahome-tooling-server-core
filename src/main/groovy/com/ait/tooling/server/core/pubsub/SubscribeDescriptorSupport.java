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

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

public class SubscribeDescriptorSupport implements Serializable
{
    private static final long                                              serialVersionUID            = -5741025269936370062L;

    private static final Logger                                            logger                      = Logger.getLogger(SubscribeDescriptorSupport.class);

    private final AtomicLong                                               m_hkey                      = new AtomicLong();

    private final ConcurrentHashMap<String, IPubSubMessageReceivedHandler> m_message_received_handlers = new ConcurrentHashMap<String, IPubSubMessageReceivedHandler>();

    public SubscribeDescriptorSupport()
    {
    }

    public void dispatch(final MessageReceivedEvent event, final ISubscribeDescriptor isubs) throws Exception
    {
        Objects.requireNonNull(event);

        Objects.requireNonNull(isubs);

        for (IPubSubMessageReceivedHandler handler : m_message_received_handlers.values())
        {
            final PubSubNextEventActionType next = handler.onMessageReceived(event);

            if (PubSubNextEventActionType.CANCEL == next)
            {
                event.cancel();
            }
            else if (PubSubNextEventActionType.REDISPATCH == next)
            {
                final IPublishDescriptor pubs = isubs.getPublishDescriptor();

                if (null != pubs)
                {
                    try
                    {
                        pubs.publish(event.getMessage().getPayload());
                    }
                    catch (Exception e)
                    {
                        logger.error("REDISPATCH threw Exception, cancelling event.", e);
                    }
                }
                else
                {
                    logger.error("REDISPATCH didnt find IPublishDescriptor, cancelling event.");
                }
                event.cancel();
            }
            if (event.isCancelled())
            {
                return;
            }
        }
    }

    public IPubSubHandlerRegistration addMessageReceivedHandler(final IPubSubMessageReceivedHandler handler)
    {
        Objects.requireNonNull(handler);

        final String hkey = Long.toString(m_hkey.incrementAndGet());

        m_message_received_handlers.put(hkey, handler);

        return new IPubSubHandlerRegistration()
        {
            private static final long serialVersionUID = -4801721135204751486L;

            @Override
            public void removeHandler()
            {
                m_message_received_handlers.remove(hkey);
            }
        };
    }
}
