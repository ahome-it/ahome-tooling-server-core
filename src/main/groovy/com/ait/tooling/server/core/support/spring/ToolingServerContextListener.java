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

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ToolingServerContextListener extends ContextLoaderListener
{
    private static final Logger logger = Logger.getLogger(ToolingServerContextListener.class);

    @Override
    public void contextInitialized(final ServletContextEvent event)
    {
        super.contextInitialized(event);

        ServerContext.get().setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(event.getServletContext()));

        logger.info("ToolingServerContextListener initialized");
    }

    @Override
    public void contextDestroyed(final ServletContextEvent event)
    {
        logger.info("ToolingServerContextListener shutting down");

        super.contextDestroyed(event);
    }
}
