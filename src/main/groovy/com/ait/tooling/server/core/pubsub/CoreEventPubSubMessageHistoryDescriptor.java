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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.ait.tooling.json.JSONObject;

public class CoreEventPubSubMessageHistoryDescriptor implements IPubSubMessageHistoryDescriptor
{
    private static final long                      serialVersionUID = -1089554093858106699L;

    private long                                   m_size;

    private long                                   m_time;

    private final String                           m_name;

    private PubSubStateType                        m_open           = PubSubStateType.CLOSED;

    private final PubSubChannelType                m_type           = PubSubChannelType.EVENT;

    private final List<IPubSubMessageHistoryEntry> m_list           = Collections.synchronizedList(new ArrayList<IPubSubMessageHistoryEntry>());

    public CoreEventPubSubMessageHistoryDescriptor()
    {
        m_name = "CoreServerEvents";

        setState(PubSubStateType.CONNECTED);
    }

    @Override
    public String getName()
    {
        return m_name;
    }

    @Override
    public void close() throws IOException
    {
        setState(PubSubStateType.CLOSED);
    }

    @Override
    public long getMaxSize()
    {
        return m_size;
    }

    @Override
    public void setMaxSize(final long size)
    {
        m_size = Math.max(0, size);
    }

    @Override
    public long getMaxTime()
    {
        return m_time;
    }

    @Override
    public void setMaxTime(final long time)
    {
        m_time = Math.max(0, time);
    }

    @Override
    public PubSubStateType getState()
    {
        return m_open;
    }

    @Override
    public void record(final IPubSubEvent<JSONObject> event) throws Exception
    {
        Objects.requireNonNull(event);

        m_list.add(new CorePubSubMessageHistoryEntry(System.currentTimeMillis(), event.getDescriptor().getName(), event.getDescriptor().getChannelType(), event.getValue()));
    }

    @Override
    public List<IPubSubMessageHistoryEntry> history()
    {
        return Collections.unmodifiableList(new ArrayList<IPubSubMessageHistoryEntry>(m_list));
    }

    @Override
    public void setState(final PubSubStateType state)
    {
        m_open = Objects.requireNonNull(state);
    }

    @Override
    public PubSubChannelType getType()
    {
        return m_type;
    }
}
