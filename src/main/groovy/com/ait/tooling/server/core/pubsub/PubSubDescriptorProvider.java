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
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import com.ait.tooling.common.api.java.util.StringOps;

public class PubSubDescriptorProvider implements IPubSubDescriptorProvider, BeanFactoryAware
{
    private static final long                                 serialVersionUID  = 4690403089220044743L;

    private static final Logger                               logger            = Logger.getLogger(PubSubDescriptorProvider.class);

    private final LinkedHashMap<String, IPublishDescriptor>   m_pub_descriptors = new LinkedHashMap<String, IPublishDescriptor>();

    private final LinkedHashMap<String, ISubscribeDescriptor> m_sub_descriptors = new LinkedHashMap<String, ISubscribeDescriptor>();

    public PubSubDescriptorProvider()
    {
    }

    private void addSubscribeDescriptor(final ISubscribeDescriptor descriptor)
    {
        if (null != descriptor)
        {
            final String name = StringOps.toTrimOrNull(descriptor.getName());

            if (null != name)
            {
                if (null == m_sub_descriptors.get(name))
                {
                    m_sub_descriptors.put(name, descriptor);

                    logger.info("PubSubDescriptorProvider.addSubscribeDescriptor(" + name + ") Registered");
                }
                else
                {
                    logger.error("PubSubDescriptorProvider.addSubscribeDescriptor(" + name + ") Duplicate ignored");
                }
            }
        }
    }

    private void addPublishDescriptor(final IPublishDescriptor descriptor)
    {
        if (null != descriptor)
        {
            final String name = StringOps.toTrimOrNull(descriptor.getName());

            if (null != name)
            {
                if (null == m_pub_descriptors.get(name))
                {
                    m_pub_descriptors.put(name, descriptor);

                    logger.info("PubSubDescriptorProvider.addPublishDescriptor(" + name + ") Registered");
                }
                else
                {
                    logger.error("PubSubDescriptorProvider.addPublishDescriptor(" + name + ") Duplicate ignored");
                }
            }
        }
    }

    @Override
    public void setBeanFactory(final BeanFactory factory) throws BeansException
    {
        if (factory instanceof DefaultListableBeanFactory)
        {
            for (IPublishDescriptor descriptor : ((DefaultListableBeanFactory) factory).getBeansOfType(IPublishDescriptor.class).values())
            {
                addPublishDescriptor(descriptor);
            }
            for (ISubscribeDescriptor descriptor : ((DefaultListableBeanFactory) factory).getBeansOfType(ISubscribeDescriptor.class).values())
            {
                addSubscribeDescriptor(descriptor);
            }
        }
    }

    @Override
    public void close() throws IOException
    {
        for (IPublishDescriptor descriptor : m_pub_descriptors.values())
        {
            try
            {
                logger.info("PubSubDescriptorProvider.IPublishDescriptor.close(" + descriptor.getName() + ")");

                descriptor.close();
            }
            catch (Exception e)
            {
                logger.error("PubSubDescriptorProvider.IPublishDescriptor.close(" + descriptor.getName() + ") ERROR ", e);
            }
        }
        for (ISubscribeDescriptor descriptor : m_sub_descriptors.values())
        {
            try
            {
                logger.info("PubSubDescriptorProvider.ISubscribeDescriptor.close(" + descriptor.getName() + ")");

                descriptor.close();
            }
            catch (Exception e)
            {
                logger.error("PubSubDescriptorProvider.ISubscribeDescriptor.close(" + descriptor.getName() + ") ERROR ", e);
            }
        }
    }

    @Override
    public IPublishDescriptor getPublishDescriptor(final String name)
    {
        return m_pub_descriptors.get(StringOps.requireTrimOrNull(name));
    }

    @Override
    public ISubscribeDescriptor getSubscribeDescriptor(final String name)
    {
        return m_sub_descriptors.get(StringOps.requireTrimOrNull(name));
    }
}
