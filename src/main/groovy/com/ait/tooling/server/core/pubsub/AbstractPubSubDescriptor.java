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

import com.ait.tooling.common.api.java.util.StringOps;

@SuppressWarnings("serial")
public abstract class AbstractPubSubDescriptor implements IPubSubDescriptor
{
    private final String            m_name;

    private final PubSubChannelType m_type;

    private PubSubStateType         m_state = PubSubStateType.CLOSED;

    protected AbstractPubSubDescriptor(final String name, final PubSubChannelType type)
    {
        m_name = StringOps.requireTrimOrNull(name);

        m_type = Objects.requireNonNull(type);
    }

    @Override
    public String getName()
    {
        return m_name;
    }

    protected void setState(final PubSubStateType state)
    {
        m_state = Objects.requireNonNull(state);
    }

    @Override
    public PubSubStateType getState()
    {
        return m_state;
    }

    @Override
    public PubSubChannelType getChannelType()
    {
        return m_type;
    }
}
