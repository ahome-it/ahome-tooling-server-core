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

@SuppressWarnings("serial")
public abstract class AbstractPubSubEvent<T extends Serializable> implements IPubSubEvent<T>
{
    private final T                 m_value;

    private final IPubSubDescriptor m_descriptor;

    private boolean                 m_cancelled = false;

    protected AbstractPubSubEvent(final IPubSubDescriptor descriptor, final T value)
    {
        m_value = Objects.requireNonNull(value);

        m_descriptor = Objects.requireNonNull(descriptor);
    }

    @Override
    public final T getValue()
    {
        return m_value;
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
    public final boolean isCancelled()
    {
        return m_cancelled;
    }

    @Override
    public final IPubSubDescriptor getDescriptor()
    {
        return m_descriptor;
    }
}
