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

import com.ait.tooling.json.JSONObject;

public class CorePubSubMessageHistoryEntry implements IPubSubMessageHistoryEntry
{
    private static final long       serialVersionUID = 2912031035860422614L;

    private final long              m_time;

    private final String            m_name;

    private final JSONObject        m_valu;

    private final PubSubChannelType m_type;

    public CorePubSubMessageHistoryEntry(final long time, final String name, final PubSubChannelType type, final JSONObject valu)
    {
        m_time = time;

        m_name = Objects.requireNonNull(name);

        m_type = Objects.requireNonNull(type);

        m_valu = Objects.requireNonNull(valu);
    }

    @Override
    public String getName()
    {
        return m_name;
    }

    @Override
    public JSONObject getValue()
    {
        return m_valu;
    }

    @Override
    public long getTime()
    {
        return m_time;
    }

    @Override
    public PubSubChannelType getChannelType()
    {
        return m_type;
    }

    @Override
    public JSONObject toJSONObject()
    {
        final JSONObject json = new JSONObject();

        json.put("time", getTime());

        json.put("name", getName());

        json.put("type", getChannelType().getValue());

        json.put("value", getValue());

        return json;
    }
}
