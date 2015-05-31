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
import java.util.Map;
import java.util.Objects;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import com.ait.tooling.common.api.json.JSONStringify;
import com.ait.tooling.json.JSONObject;

public class JSONMessage implements Message<JSONObject>, Serializable, JSONStringify
{
    private static final long    serialVersionUID = 5785132886874321325L;

    private final JSONObject     m_payload;

    private final MessageHeaders m_headers;

    public JSONMessage(final JSONObject payload, final MessageHeaders headers)
    {
        m_payload = Objects.requireNonNull(payload);

        m_headers = Objects.requireNonNull(headers);
    }

    public JSONMessage(final JSONObject payload, final Map<String, ?> headers)
    {
        this(Objects.requireNonNull(payload), new JSONObject(Objects.requireNonNull(headers)));
    }

    public JSONMessage(final JSONObject payload, final JSONObject headers)
    {
        this(Objects.requireNonNull(payload), new MessageHeaders(Objects.requireNonNull(headers)));
    }

    public JSONMessage(final JSONObject payload)
    {
        this(Objects.requireNonNull(payload), new JSONObject());
    }

    @Override
    public JSONObject getPayload()
    {
        return m_payload;
    }

    @Override
    public MessageHeaders getHeaders()
    {
        return m_headers;
    }

    @Override
    public String toJSONString()
    {
        return getPayload().toJSONString();
    }

    @Override
    public String toString()
    {
        return toJSONString();
    }
}
