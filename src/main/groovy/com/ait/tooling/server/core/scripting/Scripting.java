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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import com.ait.tooling.common.api.java.util.StringOps;

public class Scripting
{
    public final static ScriptEngine getScriptEngine(final ScriptType type)
    {
        return new ScriptEngineManager().getEngineByName(StringOps.requireTrimOrNull(type.getValue()));
    }

    public final static ScriptEngine getScriptEngine(final ScriptType type, final ClassLoader loader)
    {
        return new ScriptEngineManager(Objects.requireNonNull(loader)).getEngineByName(StringOps.requireTrimOrNull(type.getValue()));
    }

    public final static List<String> getScriptingLanguageNames()
    {
        final HashSet<String> look = new HashSet<String>();

        for (ScriptEngineFactory factory : new ScriptEngineManager().getEngineFactories())
        {
            look.addAll(factory.getNames());
        }
        final HashSet<String> find = new HashSet<String>();

        for (ScriptType type : ScriptType.values())
        {
            for (String name : look)
            {
                if (type.getValue().equalsIgnoreCase(name))
                {
                    find.add(type.getValue());
                }
            }
        }
        return Collections.unmodifiableList(new ArrayList<String>(find));
    }

    public final static List<ScriptType> getScriptingLanguageTypes()
    {
        final HashSet<String> look = new HashSet<String>();

        for (ScriptEngineFactory factory : new ScriptEngineManager().getEngineFactories())
        {
            look.addAll(factory.getNames());
        }
        final HashSet<ScriptType> find = new HashSet<ScriptType>();

        for (ScriptType type : ScriptType.values())
        {
            for (String name : look)
            {
                if (type.getValue().equalsIgnoreCase(name))
                {
                    find.add(type);
                }
            }
        }
        return Collections.unmodifiableList(new ArrayList<ScriptType>(find));
    }
}
