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

import java.lang.reflect.Field;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.ait.tooling.common.api.types.IStringValued;

public enum Scripting implements IStringValued
{
    GROOVY("groovy"), JAVASCRIPT("javascript"), PYTHON("python"), RUBY("ruby");

    static
    {
        setPythonImportSite(false);
    }

    private final String m_value;

    private Scripting(String value)
    {
        m_value = value;
    }

    @Override
    public final String getValue()
    {
        return m_value;
    }

    @Override
    public final String toString()
    {
        return m_value;
    }

    public final ScriptEngine getScriptEngine()
    {
        return getScriptEngine(getValue());
    }

    public final ScriptEngine getScriptEngine(final ClassLoader loader)
    {
        return getScriptEngine(getValue(), loader);
    }

    public static final ScriptEngine getScriptEngine(final String name)
    {
        return new ScriptEngineManager().getEngineByName(name);
    }

    public static final ScriptEngine getScriptEngine(final String name, final ClassLoader loader)
    {
        return new ScriptEngineManager(loader).getEngineByName(name);
    }

    public static final void setPythonImportSite(final boolean site)
    {
        setPythonOptions("importSite", site);
    }

    private static final void setPythonOptions(final String name, final Object value)
    {
        try
        {
            final Class<?> options = Class.forName("org.python.core.Options");

            final Field field = options.getDeclaredField(name);

            field.set(null, value);
        }
        catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }
}
