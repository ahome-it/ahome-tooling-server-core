/*
 * Copyright (c) 2014,2015,2016 Ahome' Innovation Technologies. All rights reserved.
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

package com.ait.tooling.server.core.support.instrument.telemetry;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;

import org.springframework.messaging.Message;

import com.ait.tooling.common.api.java.util.StringOps;
import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.pubsub.JSONMessageBuilder;

public class TelemetryMessage implements ITelemetryMessage
{
    private static final long       serialVersionUID = 5585132472596795023L;

    private static final JSONObject SERVER_INFO      = getServerInfo();

    private boolean                 m_closed         = false;

    private final long              m_timemark;

    private final String            m_category;

    private final Object            m_messages;

    public TelemetryMessage(final String category, final Object messages, final long timemark)
    {
        m_timemark = timemark;

        m_category = StringOps.requireTrimOrNull(category);

        m_messages = Objects.requireNonNull(messages);
    }

    @Override
    public void close() throws IOException
    {
        m_closed = true;
    }

    @Override
    public boolean isClosed()
    {
        return m_closed;
    }

    @Override
    public Object getMessage()
    {
        return m_messages;
    }

    @Override
    public long getTimeStamp()
    {
        return m_timemark;
    }

    @Override
    public String getCategory()
    {
        return m_category;
    }

    @Override
    public JSONObject toJSONObject()
    {
        return new JSONObject("TELEMETRY", new JSONObject("category", getCategory()).set("timestamp", getTimeStamp()).set("server", SERVER_INFO).set("message", getMessage()));
    }

    private static final JSONObject getServerInfo()
    {
        String host;

        String addr;

        try
        {
            host = InetAddress.getLocalHost().getHostName();
        }
        catch (Exception e)
        {
            host = "localhost:unknown";
        }
        try
        {
            addr = InetAddress.getLocalHost().getHostAddress();
        }
        catch (Exception e)
        {
            addr = "127.0.0.1:unknown";
        }
        return new JSONObject("host", host).set("addr", addr);
    }

    @Override
    public Message<JSONObject> toJSONPublishMessage()
    {
        return JSONMessageBuilder.createMessage(toJSONObject());
    }
}
