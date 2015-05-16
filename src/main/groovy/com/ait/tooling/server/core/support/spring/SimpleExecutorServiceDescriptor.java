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

import java.util.Objects;
import java.util.concurrent.ExecutorService;

public class SimpleExecutorServiceDescriptor extends AbstractExecutorServiceDescriptor
{
    private static final long serialVersionUID = 1569342132201979250L;

    private ExecutorService   m_executor;

    protected SimpleExecutorServiceDescriptor()
    {
    }

    public SimpleExecutorServiceDescriptor(final ExecutorService executor)
    {
        setExecutorService(executor);
    }

    @Override
    public ExecutorService getExecutorService()
    {
        return m_executor;
    }

    protected void setExecutorService(final ExecutorService executor)
    {
        m_executor = Objects.requireNonNull(executor);
    }
}
