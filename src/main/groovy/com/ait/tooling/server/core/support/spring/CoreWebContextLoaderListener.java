/*
 * Copyright (c) 2014,2015,2016 Ahome' Innovation Technologies. All rights reserved.
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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

public class CoreWebContextLoaderListener extends ContextLoaderListener
{
    private static final Logger logger = Logger.getLogger(CoreWebContextLoaderListener.class);

    @Override
    public void contextInitialized(final ServletContextEvent event)
    {
        logger.info("CoreWebContextLoaderListener.contextInitialized() STARTING");

        super.contextInitialized(event);

        logger.info("CoreWebContextLoaderListener.contextInitialized() COMPLETE");
    }

    @Override
    public void contextDestroyed(final ServletContextEvent event)
    {
        logger.info("CoreWebContextLoaderListener.contextDestroyed() STARTING");

        super.contextDestroyed(event);

        logger.info("CoreWebContextLoaderListener.contextDestroyed() COMPLETE");
    }

    @Override
    protected void customizeContext(final ServletContext sc, final ConfigurableWebApplicationContext context)
    {
        logger.info("CoreWebContextLoaderListener.customizeContext() STARTING");

        super.customizeContext(sc, context);

        ServerContextInstance.setApplicationContext(context);

        logger.info("CoreWebContextLoaderListener.customizeContext() COMPLETE");
    }
}
