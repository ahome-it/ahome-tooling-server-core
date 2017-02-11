/*
 * Copyright (c) 2017 Ahome' Innovation Technologies. All rights reserved.
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
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration.Dynamic;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.ait.tooling.common.api.java.util.StringOps;

public class SpringDispatcherServletContextCustomizer implements IServletContextCustomizer
{
    private static final Logger logger = Logger.getLogger(SpringDispatcherServletContextCustomizer.class);

    private final String        m_name;

    private final String[]      m_path;

    private int                 m_load;

    public SpringDispatcherServletContextCustomizer(String name, String paths)
    {
        m_name = StringOps.requireTrimOrNull(name);

        paths = StringOps.requireTrimOrNull(paths);

        if (paths.contains(","))
        {
            m_path = StringOps.toUniqueArray(StringOps.tokenizeToStringCollection(paths, ",", true, true));
        }
        else
        {
            m_path = StringOps.toUniqueArray(paths);
        }
    }

    public SpringDispatcherServletContextCustomizer(String name, Collection<String> paths)
    {
        this(name, StringOps.toCommaSeparated(paths));
    }

    public void setLoadOnStartup(int load)
    {
        m_load = load;
    }

    @Override
    public void close() throws IOException
    {
    }

    @Override
    public void customize(final ServletContext sc, final WebApplicationContext context)
    {
        logger.info("CUSTOMIZE");
        
        if (m_path.length > 0)
        {
            final Dynamic dispatcher = sc.addServlet(m_name, new DispatcherServlet(context));

            dispatcher.addMapping(m_path);

            dispatcher.setLoadOnStartup(m_load);
        }
    }
}
