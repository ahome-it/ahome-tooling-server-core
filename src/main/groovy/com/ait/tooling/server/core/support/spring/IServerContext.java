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

import org.springframework.core.env.Environment;
import org.springframework.web.context.WebApplicationContext;

import com.ait.tooling.server.core.jmx.management.IServerManager;
import com.ait.tooling.server.core.security.IAuthorizationProvider;
import com.ait.tooling.server.core.security.IAuthorizer;

public interface IServerContext extends IAuthorizer, IPropertiesResolver
{
    public IServerContext getServerContext();

    public WebApplicationContext getApplicationContext();

    public Environment getEnvironment();

    public IAuthorizationProvider getAuthorizationProvider();

    public Iterable<String> getPrincipalsKeys();

    public IServerManager getServerManager();

    public IExecutorServiceDescriptorProvider getExecutorServiceDescriptorProvider();

    public IBuildDescriptorProvider getBuildDescriptorProvider();

    public IPropertiesResolver getPropertiesResolver();
}