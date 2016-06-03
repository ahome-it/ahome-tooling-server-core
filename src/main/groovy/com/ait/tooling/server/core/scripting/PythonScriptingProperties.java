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

import java.util.Map;
import java.util.Objects;

import org.python.core.PySystemState;
import org.springframework.core.io.Resource;

import com.ait.tooling.common.api.java.util.StringOps;

public class PythonScriptingProperties extends AbstractScriptingProperties
{
    public PythonScriptingProperties()
    {
        super(ScriptType.PYTHON);
    }

    public PythonScriptingProperties(final Map<String, String> properties)
    {
        this();

        populate(Objects.requireNonNull(properties));
    }

    public PythonScriptingProperties(final Resource resource) throws Exception
    {
        this();

        populate(Objects.requireNonNull(resource));
    }

    @Override
    protected void start()
    {
        getProperties().putIfAbsent("python.home", StringOps.toTrimOrElse(System.getenv("JYTHON_HOME"), "/opt/development/jython/Lib"));

        getProperties().putIfAbsent("python.console.encoding", "UTF-8");

        getProperties().putIfAbsent("python.security.respectJavaAccessibility", "false");

        getProperties().putIfAbsent("python.import.site", "false");

        PySystemState.initialize(PySystemState.getBaseProperties(), getProperties(), StringOps.EMPTY_STRING_ARRAY);
    }
}
