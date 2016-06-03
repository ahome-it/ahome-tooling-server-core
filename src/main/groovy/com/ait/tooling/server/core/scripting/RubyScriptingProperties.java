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

import org.springframework.core.io.Resource;

import com.ait.tooling.common.api.java.util.StringOps;

public class RubyScriptingProperties extends AbstractScriptingProperties
{
    public RubyScriptingProperties()
    {
        super(ScriptType.RUBY);
    }

    public RubyScriptingProperties(final Map<String, String> properties)
    {
        this();

        populate(Objects.requireNonNull(properties));
    }

    public RubyScriptingProperties(final Resource resource) throws Exception
    {
        this();

        populate(Objects.requireNonNull(resource));
    }

    @Override
    protected void start()
    {
        getProperties().putIfAbsent("jruby.home", StringOps.toTrimOrElse(System.getenv("JRUBY_HOME"), "/opt/development/jruby"));

        getProperties().forEach((k, v) -> {
            properties(k, v);
        });
    }

    private final void properties(Object k, Object v)
    {
        if ((v != null) && (k != null))
        {
            final String key = k.toString();

            final String val = v.toString();

            if (key.contains("jruby"))
            {
                try
                {
                    System.setProperty(key, val);
                }
                catch (Exception e)
                {
                    logger().error("Error setting Ruby property(" + key + "," + val + ")", e);
                }
            }
        }
    }
}
