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

import com.ait.tooling.common.api.java.util.StringOps;
import com.ait.tooling.common.api.types.IStringValued;

public enum PubSubChannelType implements IStringValued
{
    QUEUE("QUEUE"), TOPIC("TOPIC"), EVENT("EVENT");

    private final String m_value;

    private PubSubChannelType(final String value)
    {
        m_value = value;
    }

    @Override
    public String getValue()
    {
        return m_value;
    }

    public static final PubSubChannelType fromString(final String valu)
    {
        final String look = StringOps.toTrimOrNull(valu);

        if (null != look)
        {
            for (PubSubChannelType type : values())
            {
                if (type.getValue().equalsIgnoreCase(look))
                {
                    return type;
                }
            }
        }
        return null;
    }
}
