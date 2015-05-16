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

package com.ait.tooling.server.core.support.spring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

import com.ait.tooling.common.api.java.util.StringOps;

@ManagedResource(objectName = "com.ait.tooling.server.core.support.spring:name=ExecutorServiceDescriptorProvider", description = "Manage Executor Service Descriptors.")
public class ExecutorServiceDescriptorProvider implements IExecutorServiceDescriptorProvider, BeanFactoryAware
{
    private static final long                                       serialVersionUID = -3904263001568352317L;

    private static final Logger                                     logger           = Logger.getLogger(ExecutorServiceDescriptorProvider.class);

    private final LinkedHashMap<String, IExecutorServiceDescriptor> m_descriptors    = new LinkedHashMap<String, IExecutorServiceDescriptor>();

    public ExecutorServiceDescriptorProvider()
    {
    }

    protected void addDescriptor(final IExecutorServiceDescriptor descriptor)
    {
        if (null != descriptor)
        {
            final String name = StringOps.toTrimOrNull(descriptor.getName());

            if (null != name)
            {
                if (null == m_descriptors.get(name))
                {
                    m_descriptors.put(name, descriptor);

                    logger.info("ExecutorServiceDescriptorProvider.addDescriptor(" + name + ") Registered");
                }
                else
                {
                    logger.error("ExecutorServiceDescriptorProvider.addDescriptor(" + name + ") Duplicate ignored");
                }
            }
        }
    }

    @Override
    public IExecutorServiceDescriptor getExecutorServiceDescriptor(final String name)
    {
        return m_descriptors.get(StringOps.requireTrimOrNull(name));
    }

    @Override
    public List<String> getExecutorServiceDescriptorNames()
    {
        return Collections.unmodifiableList(new ArrayList<String>(m_descriptors.keySet()));
    }

    @Override
    public List<IExecutorServiceDescriptor> getExecutorServiceDescriptors()
    {
        return Collections.unmodifiableList(new ArrayList<IExecutorServiceDescriptor>(m_descriptors.values()));
    }

    @Override
    public void setBeanFactory(final BeanFactory factory) throws BeansException
    {
        if (factory instanceof DefaultListableBeanFactory)
        {
            for (String name : ((DefaultListableBeanFactory) factory).getBeansOfType(IExecutorServiceDescriptor.class).keySet())
            {
                name = StringOps.toTrimOrNull(name);

                if (null != name)
                {
                    final IExecutorServiceDescriptor descriptor = factory.getBean(name, IExecutorServiceDescriptor.class);

                    if (null != descriptor)
                    {
                        descriptor.setName(name);

                        logger.info("Found IExecutorServiceDescriptor(" + name + ") class " + descriptor.getClass().getName());

                        addDescriptor(descriptor);
                    }
                }
            }
        }
    }

    @Override
    @ManagedOperation(description = "Close all ExecutorServiceDescriptors")
    public void close() throws IOException
    {
        for (IExecutorServiceDescriptor descriptor : m_descriptors.values())
        {
            if (null != descriptor)
            {
                try
                {
                    descriptor.close();
                }
                catch (IOException e)
                {
                    logger.error("ExecutorServiceDescriptor(" + descriptor.getName() + ").close()", e);
                }
            }
        }
    }
}
