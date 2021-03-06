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

package com.ait.tooling.server.core.scripting;

import java.util.Map;
import java.util.Objects;

import org.springframework.core.io.Resource;

public class GroovyScriptingProperties extends AbstractScriptingProperties
{
    public GroovyScriptingProperties()
    {
        super(ScriptType.GROOVY);
    }

    public GroovyScriptingProperties(final Map<String, String> properties)
    {
        this();
        
        populate(Objects.requireNonNull(properties));
    }

    public GroovyScriptingProperties(final Resource resource) throws Exception
    {
        this();

        populate(Objects.requireNonNull(resource));
    }
}
