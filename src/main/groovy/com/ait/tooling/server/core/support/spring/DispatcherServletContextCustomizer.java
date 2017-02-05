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

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.ait.tooling.common.api.java.util.StringOps;

public class DispatcherServletContextCustomizer implements IServletContextCustomizer
{
    private final String m_name;

    private final String m_path;

    public DispatcherServletContextCustomizer(final String name, final String path)
    {
        m_name = StringOps.requireTrimOrNull(name);

        m_path = StringOps.requireTrimOrNull(path);
    }

    @Override
    public void customize(final ServletContext sc, final ConfigurableWebApplicationContext context)
    {
        final Dynamic dispatcher = sc.addServlet(m_name, new DispatcherServlet(context));

        dispatcher.setLoadOnStartup(1);

        dispatcher.addMapping(m_path);
    }

    @Override
    public void close() throws IOException
    {
    }
}
