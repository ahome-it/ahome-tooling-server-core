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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public final class PubSubHandlerRegistrationProxy implements IPubSubHandlerRegistration, Iterable<IPubSubHandlerRegistration>
{
    private static final long                                      serialVersionUID = -109586196122482271L;

    private final CopyOnWriteArrayList<IPubSubHandlerRegistration> m_list           = new CopyOnWriteArrayList<IPubSubHandlerRegistration>();

    public PubSubHandlerRegistrationProxy()
    {
    }

    public PubSubHandlerRegistrationProxy(final IPubSubHandlerRegistration... list)
    {
        this(Arrays.asList(list));
    }

    public PubSubHandlerRegistrationProxy(final List<? extends IPubSubHandlerRegistration> list)
    {
        for (IPubSubHandlerRegistration registration : Objects.requireNonNull(list))
        {
            add(registration);
        }
    }

    public final int size()
    {
        return m_list.size();
    }

    public final boolean isEmpty()
    {
        return m_list.isEmpty();
    }

    public final void clear()
    {
        m_list.clear();
    }

    public final void destroy()
    {
        final ArrayList<IPubSubHandlerRegistration> list = new ArrayList<IPubSubHandlerRegistration>(m_list);

        for (IPubSubHandlerRegistration registration : list)
        {
            registration.removeHandler();

            m_list.remove(registration);
        }
    }

    public final void add(final IPubSubHandlerRegistration registration)
    {
        if (false == m_list.contains(Objects.requireNonNull(registration)))
        {
            m_list.add(registration);
        }
    }

    public final void remove(final IPubSubHandlerRegistration registration)
    {
        if (m_list.contains(Objects.requireNonNull(registration)))
        {
            m_list.remove(registration);
        }
    }

    @Override
    public final void removeHandler()
    {
        destroy();
    }

    @Override
    public final Iterator<IPubSubHandlerRegistration> iterator()
    {
        return Collections.unmodifiableList(new ArrayList<IPubSubHandlerRegistration>(m_list)).iterator();
    }
}
