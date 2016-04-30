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

package com.ait.tooling.server.core.scripting

import javax.script.*

import org.springframework.core.io.Resource

import com.ait.tooling.server.core.support.CoreGroovySupport

class ScriptingProxy extends CoreGroovySupport
{
    private final def m_getter = new Object()

    private final ScriptEngine m_engine

    public ScriptingProxy(final ScriptType type, final Resource resource)
    {
        this(type, resource.getInputStream())
    }

    public ScriptingProxy(final ScriptType type, final InputStream stream)
    {
        this(type, InputStreamReader(stream))
    }

    public ScriptingProxy(final ScriptType type, final Reader reader)
    {
        m_engine = scripting(type)

        m_engine.eval(reader)

        reader.close()
    }

    public ScriptingProxy(final ScriptEngine engine)
    {
        m_engine = engine
    }

    def methodMissing(String name, args)
    {
        if ((null == args) || (args.size() == 0))
        {
            return m_engine.eval("${name}()")
        }
        else
        {
            def make = ""

            def i = 1

            def bind = m_engine.getBindings(ScriptContext.ENGINE_SCOPE)

            args.each { arg ->

                def vars = "a${i}"

                i++

                make = make + "${vars},"

                bind.put(vars, arg)
            }
            make = make.substring(0, make.length() - 1)

            return m_engine.eval("${name}(${make})")
        }
    }

    def propertyMissing(String name, value = m_getter)
    {
        if (value == m_getter)
        {
            return m_engine.get(name)
        }
        else
        {
            return m_engine.put(name, value)
        }
    }
}
