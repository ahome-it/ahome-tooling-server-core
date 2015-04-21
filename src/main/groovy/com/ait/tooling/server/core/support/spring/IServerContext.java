/*
   Copyright (c) 2014,2015 Ahome' Innovation Technologies. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.ait.tooling.server.core.support.spring;

import org.springframework.core.env.Environment;
import org.springframework.web.context.WebApplicationContext;

import com.ait.tooling.server.core.jmx.management.IServerManager;
import com.ait.tooling.server.core.rpc.IJSONCommand;
import com.ait.tooling.server.core.security.IAuthorizationProvider;

public interface IServerContext
{
    public WebApplicationContext getApplicationContext();

    public Environment getEnvironment();

    public <T> T getBean(String name, Class<T> type);

    public ICommandRegistry getCommandRegistry();

    public IJSONCommand getCommand(String name);

    public IPropertiesProvider getPropertiesProvider();

    public String getPropertyByName(String name);

    public String getPropertyByName(String name, String otherwise);

    public IAuthorizationProvider getAuthorizationProvider();

    public Iterable<String> getPrincipalsKeys();

    public IServerManager getServerManager();
    
    public IServerContextModule<? extends IServerContext> getModule(String id);
    
    public <T extends IServerContext> T getModuleContext(String id, Class<T> type);
}