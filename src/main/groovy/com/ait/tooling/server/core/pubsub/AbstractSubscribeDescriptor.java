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
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.ait.tooling.common.api.types.Activatable;

@SuppressWarnings("serial")
public abstract class AbstractSubscribeDescriptor extends Activatable implements ISubscribeDescriptor
{
    private final String                             m_name;

    private final PubSubChannelType                  m_type;

    private final SubscribeDescriptorSupport         m_supp   = new SubscribeDescriptorSupport();

    private final ArrayList<IMessageReceivedHandler> m_list   = new ArrayList<IMessageReceivedHandler>();

    private final Logger                             m_logger = Logger.getLogger(getClass());

    protected AbstractSubscribeDescriptor(final String name, final PubSubChannelType type)
    {
        m_name = Objects.requireNonNull(name);

        m_type = Objects.requireNonNull(type);
    }

    protected final List<IMessageReceivedHandler> getMessageReceivedHandlers()
    {
        return m_list;
    }

    protected final SubscribeDescriptorSupport getSubscribeDescriptorSupport()
    {
        return m_supp;
    }

    @Override
    public PubSubChannelType getChannelType()
    {
        return m_type;
    }

    @Override
    public String getName()
    {
        return m_name;
    }

    @Override
    public IMessageReceivedHandlerRegistration addMessageReceivedHandler(final IMessageReceivedHandler handler)
    {
        return m_supp.addMessageReceivedHandler(Objects.requireNonNull(handler));
    }

    @Override
    public void close() throws IOException
    {
        setActive(false);

        getSubscribeDescriptorSupport().close();
    }

    public final Logger logger()
    {
        return m_logger;
    }

    public void setSubscribeListener(final IMessageReceivedHandler listener)
    {
        m_list.clear();

        m_list.add(Objects.requireNonNull(listener));
    }

    public void setSubscribeListeners(final List<IMessageReceivedHandler> listeners)
    {
        m_list.clear();

        for (IMessageReceivedHandler listener : Objects.requireNonNull(listeners))
        {
            m_list.add(Objects.requireNonNull(listener));
        }
    }
}
