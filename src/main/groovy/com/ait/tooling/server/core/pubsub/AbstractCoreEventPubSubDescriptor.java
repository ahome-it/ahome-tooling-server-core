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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.ait.tooling.json.JSONObject;

@SuppressWarnings("serial")
public abstract class AbstractCoreEventPubSubDescriptor extends AbstractPubSubDescriptor
{
    private final AtomicLong                                               m_long                      = new AtomicLong();

    private final ConcurrentHashMap<String, IPubSubStateChangedHandler>    m_state_changed_handlers    = new ConcurrentHashMap<String, IPubSubStateChangedHandler>();

    private final ConcurrentHashMap<String, IPubSubMessageReceivedHandler> m_message_received_handlers = new ConcurrentHashMap<String, IPubSubMessageReceivedHandler>();

    protected AbstractCoreEventPubSubDescriptor()
    {
        super(PubSubChannelType.EVENT);
    }

    @Override
    public JSONObject publish(final JSONObject message) throws Exception
    {
        if (PubSubStateType.CONNECTED != getState())
        {
            throw new PubSubException("PubSubDescriptor.name(" + getName() + ") is not connected [" + getState() + "]");
        }
        final MessageReceivedEvent event = new MessageReceivedEvent(this, Objects.requireNonNull(message));

        for (IPubSubMessageReceivedHandler handler : m_message_received_handlers.values())
        {
            if (PubSubStateType.CONNECTED == getState())
            {
                final PubSubNextEventActionType next = handler.onMesageReceived(event);

                if (PubSubNextEventActionType.CANCEL == next)
                {
                    event.cancel();
                }
                if (event.isCancelled())
                {
                    break;
                }
            }
        }
        return message;
    }

    @Override
    public IPubSubHandlerRegistration addStateChangedHandler(final IPubSubStateChangedHandler handler)
    {
        Objects.requireNonNull(handler);

        final String hkey = Long.toString(m_long.incrementAndGet());

        m_state_changed_handlers.put(hkey, handler);

        return new IPubSubHandlerRegistration()
        {
            @Override
            public void removeHandler()
            {
                m_state_changed_handlers.remove(hkey);
            }
        };
    }

    @Override
    public IPubSubHandlerRegistration addMessageReceivedHandler(final IPubSubMessageReceivedHandler handler)
    {
        Objects.requireNonNull(handler);

        final String hkey = Long.toString(m_long.incrementAndGet());
        
        m_message_received_handlers.put(hkey, handler);

        return new IPubSubHandlerRegistration()
        {
            @Override
            public void removeHandler()
            {
                m_message_received_handlers.remove(hkey);
            }
        };
    }

    @Override
    protected void setState(final PubSubStateType state)
    {
        if (Objects.requireNonNull(state) != (Objects.requireNonNull(getState())))
        {
            super.setState(state);

            final StateChangedEvent event = new StateChangedEvent(this, Objects.requireNonNull(state));

            for (IPubSubStateChangedHandler handler : m_state_changed_handlers.values())
            {
                final PubSubNextEventActionType next = handler.onStateChanged(event);

                if (PubSubNextEventActionType.CANCEL == next)
                {
                    event.cancel();
                }
                if (event.isCancelled())
                {
                    return;
                }
            }
        }
    }

    @Override
    public void close() throws IOException
    {
        setState(PubSubStateType.CLOSED);
    }
}
