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

package com.ait.tooling.server.core.scripting;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.python.util.PythonInterpreter;
import org.springframework.core.io.Resource;

import com.ait.tooling.common.api.types.Activatable;

public class PythonScriptingProperties extends Activatable implements IScriptingProperties
{
    private final Properties m_prop = new Properties();
    
    public PythonScriptingProperties()
    {
        setup();
    }

    public PythonScriptingProperties(final Map<String, String> conf)
    {
        m_prop.putAll(conf);

        setup();
    }

    public PythonScriptingProperties(final Resource resource) throws IOException
    {
        m_prop.load(resource.getInputStream());

        setup();
    }

    private final void setup()
    {
        m_prop.putIfAbsent("python.home", "/Development/jython2.7.0/Lib");

        m_prop.putIfAbsent("python.console.encoding", "UTF-8");

        m_prop.putIfAbsent("python.security.respectJavaAccessibility", "false");

        m_prop.putIfAbsent("python.import.site", "false");

        PythonInterpreter.initialize(System.getProperties(), m_prop, new String[0]);
        
        setActive(true);
    }

    @Override
    public ScriptType getType()
    {
        return ScriptType.PYTHON;
    }

    @Override
    public void close() throws IOException
    {
        setActive(false);
    }
}
