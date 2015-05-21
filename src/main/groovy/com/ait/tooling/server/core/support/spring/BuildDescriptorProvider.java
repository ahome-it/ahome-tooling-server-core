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
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

import com.ait.tooling.common.api.java.util.StringOps;
import com.ait.tooling.json.JSONArray;

@ManagedResource(objectName = "com.ait.tooling.server.core.support.spring:name=BuildDescriptorProvider", description = "Manage Build Descriptors.")
public class BuildDescriptorProvider implements IBuildDescriptorProvider, BeanFactoryAware
{
    private static final long                             serialVersionUID = -2989587698018544105L;

    private static final Logger                           logger           = Logger.getLogger(BuildDescriptorProvider.class);

    private final LinkedHashMap<String, IBuildDescriptor> m_descriptors    = new LinkedHashMap<String, IBuildDescriptor>();

    public BuildDescriptorProvider()
    {
    }

    protected void addDescriptor(final IBuildDescriptor descriptor)
    {
        if (null != descriptor)
        {
            final String name = StringOps.toTrimOrNull(descriptor.getNameSpace());

            if (null != name)
            {
                if (null == m_descriptors.get(name))
                {
                    m_descriptors.put(name, descriptor);

                    logger.info("BuildDescriptorProvider.addDescriptor(" + name + ") Registered");
                }
                else
                {
                    logger.error("BuildDescriptorProvider.addDescriptor(" + name + ") Duplicate ignored");
                }
            }
        }
    }

    @Override
    public IBuildDescriptor getBuildDescriptor(final String name)
    {
        return m_descriptors.get(StringOps.requireTrimOrNull(name));
    }

    @Override
    @ManagedAttribute(description = "Get BuildDescriptor names.")
    public List<String> getBuildDescriptorNames()
    {
        return Collections.unmodifiableList(new ArrayList<String>(m_descriptors.keySet()));
    }

    @Override
    public List<IBuildDescriptor> getBuildDescriptors()
    {
        return Collections.unmodifiableList(new ArrayList<IBuildDescriptor>(m_descriptors.values()));
    }

    @Override
    public JSONArray getBuildDescriptorsAsJSONArray()
    {
        final JSONArray list = new JSONArray(m_descriptors.size());

        for (IBuildDescriptor descriptor : m_descriptors.values())
        {
            list.add(descriptor.getAsJSONObject());
        }
        return list;
    }
    
    @ManagedAttribute(description = "Get BuildDescriptor as JSONArray String.")
    public String getBuildDescriptorsAsJSONArrayString()
    {
        return getBuildDescriptorsAsJSONArray().toJSONString();
    }

    @Override
    public void setBeanFactory(final BeanFactory factory) throws BeansException
    {
        if (factory instanceof DefaultListableBeanFactory)
        {
            for (IBuildDescriptor descriptor : ((DefaultListableBeanFactory) factory).getBeansOfType(IBuildDescriptor.class).values())
            {
                addDescriptor(descriptor);
            }
        }
    }

    @Override
    public void close() throws IOException
    {
    }
}
