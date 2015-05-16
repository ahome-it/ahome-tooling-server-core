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
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.ait.tooling.common.api.java.util.StringOps;

@SuppressWarnings("serial")
public abstract class AbstractExecutorServiceDescriptor implements IExecutorServiceDescriptor
{
    protected static final int DEFAULT_THREAD_POOL_SIZE = 2;

    protected static final int MAXIMUM_THREAD_POOL_SIZE = 128;

    private String             m_name;

    public AbstractExecutorServiceDescriptor()
    {
    }

    @Override
    public String getName()
    {
        return m_name;
    }

    @Override
    public void close() throws IOException
    {
    }

    @Override
    public void setName(final String name)
    {
        m_name = StringOps.requireTrimOrNull(name);
    }

    @Override
    public ThreadFactory getThreadFactory()
    {
        return Executors.defaultThreadFactory();
    }
}
