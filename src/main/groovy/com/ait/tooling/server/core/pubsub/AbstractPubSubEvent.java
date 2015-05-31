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

import java.util.Map;
import java.util.Objects;

import org.springframework.integration.event.core.MessagingEvent;
import org.springframework.messaging.MessageHeaders;

import com.ait.tooling.json.JSONObject;

@SuppressWarnings("serial")
public abstract class AbstractPubSubEvent extends MessagingEvent implements IPubSubEvent
{
    private final IPubSubDescriptor m_descriptor;

    private boolean                 m_cancelled = false;

    protected AbstractPubSubEvent(final IPubSubDescriptor descriptor, final JSONObject payload)
    {
        super(new JSONMessage(Objects.requireNonNull(payload)));

        m_descriptor = Objects.requireNonNull(descriptor);
    }

    protected AbstractPubSubEvent(final IPubSubDescriptor descriptor, final JSONObject payload, final MessageHeaders headers)
    {
        super(new JSONMessage(Objects.requireNonNull(payload), Objects.requireNonNull(headers)));

        m_descriptor = Objects.requireNonNull(descriptor);
    }

    protected AbstractPubSubEvent(final IPubSubDescriptor descriptor, final JSONObject payload, final Map<String, ?> headers)
    {
        super(new JSONMessage(Objects.requireNonNull(payload), Objects.requireNonNull(headers)));

        m_descriptor = Objects.requireNonNull(descriptor);
    }

    protected AbstractPubSubEvent(final IPubSubDescriptor descriptor, final JSONObject payload, final JSONObject headers)
    {
        super(new JSONMessage(Objects.requireNonNull(payload), Objects.requireNonNull(headers)));

        m_descriptor = Objects.requireNonNull(descriptor);
    }

    @Override
    public final boolean cancel()
    {
        if (false == m_cancelled)
        {
            m_cancelled = true;

            return true;
        }
        return false;
    }

    @Override
    public JSONMessage getMessage()
    {
        return ((JSONMessage) super.getMessage());
    }

    @Override
    public final boolean isCancelled()
    {
        return m_cancelled;
    }

    @Override
    public final IPubSubDescriptor getDescriptor()
    {
        return m_descriptor;
    }

    @Override
    public MessageHeaders getHeaders()
    {
        return getMessage().getHeaders();
    }
}
