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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.jmx.export.annotation.ManagedResource;

import com.ait.tooling.common.api.java.util.StringOps;

@ManagedResource(objectName = "com.ait.tooling.server.core.pubsub:name=PubSubDescriptorProvider", description = "Manage PubSub Descriptors.")
public class PubSubDescriptorProvider implements IPubSubDescriptorProvider, BeanFactoryAware
{
    private static final long                                 serialVersionUID = 4690403089220044743L;

    private static final Logger                               logger           = Logger.getLogger(PubSubDescriptorProvider.class);

    private final LinkedHashMap<String, IPubSubDescriptor<?>> m_descriptors    = new LinkedHashMap<String, IPubSubDescriptor<?>>();

    public PubSubDescriptorProvider()
    {
    }

    protected void addDescriptor(final IPubSubDescriptor<?> descriptor)
    {
        if (null != descriptor)
        {
            final String name = StringOps.toTrimOrNull(descriptor.getName());

            if (null != name)
            {
                if (null == m_descriptors.get(name))
                {
                    m_descriptors.put(name, descriptor);

                    logger.info("PubSubDescriptorProvider.addDescriptor(" + name + ") Registered");
                }
                else
                {
                    logger.error("PubSubDescriptorProvider.addDescriptor(" + name + ") Duplicate ignored");
                }
            }
        }
    }

    @Override
    public List<String> getPubSubDescriptorNames()
    {
        return Collections.unmodifiableList(new ArrayList<String>(m_descriptors.keySet()));
    }

    @Override
    public List<IPubSubDescriptor<?>> getPubSubDescriptors()
    {
        return Collections.unmodifiableList(new ArrayList<IPubSubDescriptor<?>>(m_descriptors.values()));
    }

    @Override
    public void setBeanFactory(final BeanFactory factory) throws BeansException
    {
        if (factory instanceof DefaultListableBeanFactory)
        {
            for (IPubSubDescriptor<?> descriptor : ((DefaultListableBeanFactory) factory).getBeansOfType(IPubSubDescriptor.class).values())
            {
                addDescriptor(descriptor);
            }
        }
    }

    @Override
    public void close() throws IOException
    {
        for (IPubSubDescriptor<?> descriptor : m_descriptors.values())
        {
            try
            {
                logger.info("PubSubDescriptorProvider.close(" + descriptor.getName() + ")");

                descriptor.close();
            }
            catch (Exception e)
            {
                logger.error("PubSubDescriptorProvider.close(" + descriptor.getName() + ") ERROR ", e);
            }
        }
    }

    @Override
    public <T extends Serializable> IPubSubDescriptor<T> getPubSubDescriptor(final String name, final Class<T> type, final PubSubChannelType... list)
    {
        return getPubSubDescriptor(name, type, Arrays.asList(list));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Serializable> IPubSubDescriptor<T> getPubSubDescriptor(final String name, final Class<T> type, final List<PubSubChannelType> list)
    {
        final IPubSubDescriptor<?> descriptor = m_descriptors.get(StringOps.requireTrimOrNull(name));

        if (null != descriptor)
        {
            if (false == Objects.requireNonNull(type).isAssignableFrom(Objects.requireNonNull(descriptor.getMessageType())))
            {
                return null;
            }
            if ((null != list) && (false == list.isEmpty()))
            {
                final List<PubSubChannelType> have = Objects.requireNonNull(descriptor.getChannelTypes());

                for (PubSubChannelType look : list)
                {
                    if (false == have.contains(look))
                    {
                        return null;
                    }
                }
            }
            return ((IPubSubDescriptor<T>) descriptor);
        }
        return null;
    }
}
