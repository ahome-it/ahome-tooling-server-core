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
import java.util.List;
import java.util.Objects;

public class PubSubProvider implements IPubSubProvider
{
    private static final long         serialVersionUID = -3317663412767450650L;

    private IPubSubDescriptorProvider m_descriptor_provider;

    public PubSubProvider(final IPubSubDescriptorProvider provider)
    {
        m_descriptor_provider = Objects.requireNonNull(provider);
    }

    @Override
    public IPubSubDescriptorProvider getPubSubDescriptorProvider()
    {
        return Objects.requireNonNull(m_descriptor_provider, "PubSubDescriptorProvider is null, initialization error.");
    }

    @Override
    public final void publish(String name, JSONMessage message) throws Exception
    {
        Objects.requireNonNull(name);

        Objects.requireNonNull(message);

        final IPublishDescriptor desc = getPubSubDescriptorProvider().getPublishDescriptor(name);

        if (null != desc)
        {
            desc.publish(message);
        }
        else
        {
            throw new PubSubException("IPublishDescriptor " + name + " not found");
        }
    }

    @Override
    public final void publish(String name, PubSubChannelType type, JSONMessage message) throws Exception
    {
        name = Objects.requireNonNull(name);

        type = Objects.requireNonNull(type);

        message = Objects.requireNonNull(message);

        final IPublishDescriptor desc = getPubSubDescriptorProvider().getPublishDescriptor(name, type);

        if (null != desc)
        {
            desc.publish(message);
        }
        else
        {
            throw new PubSubException("IPublishDescriptor " + name + " type " + type.getValue() + " not found");
        }
    }

    @Override
    public final void publish(String name, List<PubSubChannelType> list, JSONMessage message) throws Exception
    {
        name = Objects.requireNonNull(name);

        list = Objects.requireNonNull(list);

        message = Objects.requireNonNull(message);

        final IPublishDescriptor desc = getPubSubDescriptorProvider().getPublishDescriptor(name, list);

        if (null != desc)
        {
            desc.publish(message);
        }
        else
        {
            StringBuilder builder = new StringBuilder();

            for (PubSubChannelType type : list)
            {
                builder.append(type.getValue());

                builder.append(",");
            }
            final int last = builder.lastIndexOf(",");

            if (last > 0)
            {
                builder.deleteCharAt(last);
            }
            throw new PubSubException("IPublishDescriptor " + name + " of types [" + builder.toString() + "] not found");
        }
    }

    @Override
    public final IPubSubHandlerRegistration addMessageReceivedHandler(String name, IPubSubMessageReceivedHandler handler) throws Exception
    {
        name = Objects.requireNonNull(name);

        handler = Objects.requireNonNull(handler);

        final ISubscribeDescriptor desc = getPubSubDescriptorProvider().getSubscribeDescriptor(name);

        if (null != desc)
        {
            return desc.addMessageReceivedHandler(handler);
        }
        else
        {
            throw new PubSubException("ISubscribeDescriptor " + name + " not found");
        }
    }

    @Override
    public final IPubSubHandlerRegistration addMessageReceivedHandler(String name, PubSubChannelType type, IPubSubMessageReceivedHandler handler) throws Exception
    {
        name = Objects.requireNonNull(name);

        type = Objects.requireNonNull(type);

        handler = Objects.requireNonNull(handler);

        final ISubscribeDescriptor desc = getPubSubDescriptorProvider().getSubscribeDescriptor(name, type);

        if (null != desc)
        {
            return desc.addMessageReceivedHandler(handler);
        }
        else
        {
            throw new PubSubException("ISubscribeDescriptor " + name + " type " + type.getValue() + " not found");
        }
    }

    @Override
    public final IPubSubHandlerRegistration addMessageReceivedHandler(String name, List<PubSubChannelType> list, IPubSubMessageReceivedHandler handler) throws Exception
    {
        name = Objects.requireNonNull(name);

        list = Objects.requireNonNull(list);

        handler = Objects.requireNonNull(handler);

        final ISubscribeDescriptor desc = getPubSubDescriptorProvider().getSubscribeDescriptor(name, list);

        if (null != desc)
        {
            return desc.addMessageReceivedHandler(handler);
        }
        else
        {
            StringBuilder builder = new StringBuilder();

            for (PubSubChannelType type : list)
            {
                builder.append(type.getValue());

                builder.append(",");
            }
            final int last = builder.lastIndexOf(",");

            if (last > 0)
            {
                builder.deleteCharAt(last);
            }
            throw new PubSubException("ISubscribeDescriptor " + name + " of types [" + builder.toString() + "] not found");
        }
    }

    @Override
    public void close() throws IOException
    {
    }
}
