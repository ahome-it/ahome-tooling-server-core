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

package com.ait.tooling.server.core.support.spring.network.websocket

import org.springframework.stereotype.Service

import com.ait.tooling.common.api.java.util.StringOps
import com.ait.tooling.common.api.types.INamedType
import com.ait.tooling.server.core.json.JSONObject
import com.ait.tooling.server.core.support.CoreGroovySupport

import groovy.transform.CompileStatic
import groovy.transform.Memoized

@CompileStatic
public abstract class WebSocketServiceSupport extends CoreGroovySupport implements IWebSocketService, INamedType
{
    private String      m_name
    
    private JSONObject  m_attr = json()
    
    protected WebSocketServiceSupport()
    {
    }

    public String getName()
    {
        if (m_name)
        {
            return m_name
        }
        final Class<?> claz = getClass()

        if (claz.isAnnotationPresent(Service))
        {
            final String name = StringOps.toTrimOrNull(claz.getAnnotation(Service).value())

            if (name)
            {
                return name
            }
        }
        claz.getSimpleName()
    }

    @Memoized
    public boolean isText()
    {
        true
    }
    
    @Override
    public JSONObject getAttributes()
    {
        m_attr
    }

    @Override
    public void setName(final String name)
    {
        m_name = name
    }
}
