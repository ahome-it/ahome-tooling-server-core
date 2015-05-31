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

import org.springframework.messaging.MessageHeaders;

import com.ait.tooling.json.JSONObject;

public class MessageReceivedEvent extends AbstractPubSubEvent
{
    private static final long serialVersionUID = -1895272847273309384L;

    public MessageReceivedEvent(final IPubSubDescriptor descriptor, final JSONObject payload)
    {
        super(Objects.requireNonNull(descriptor), Objects.requireNonNull(payload));
    }
    
    public MessageReceivedEvent(final IPubSubDescriptor descriptor, final JSONObject payload, final MessageHeaders headers)
    {
        super(Objects.requireNonNull(descriptor), Objects.requireNonNull(payload), Objects.requireNonNull(headers));
    }
    
    public MessageReceivedEvent(final IPubSubDescriptor descriptor, final JSONObject payload, final Map<String, ?> headers)
    {
        super(Objects.requireNonNull(descriptor), Objects.requireNonNull(payload), Objects.requireNonNull(headers));
    }
    
    public MessageReceivedEvent(final IPubSubDescriptor descriptor, final JSONObject payload, final JSONObject headers)
    {
        super(Objects.requireNonNull(descriptor), Objects.requireNonNull(payload), Objects.requireNonNull(headers));
    }
}
