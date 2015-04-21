/*
   Copyright (c) 2014,2015 Ahome' Innovation Technologies. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.ait.tooling.server.core.rpc

import com.ait.tooling.common.api.java.util.StringOps
import com.ait.tooling.json.JSONObject
import com.ait.tooling.json.schema.JSONSchema
import com.ait.tooling.server.core.support.CoreGroovySupport
import groovy.transform.CompileStatic

import org.springframework.stereotype.Service

@CompileStatic
public abstract class CoreGroovyCommandSupport extends CoreGroovySupport implements IJSONCommand
{
    @Override
    public String getName()
    {
        Class<?> type = getClass()

        if (type.isAnnotationPresent(Service.class))
        {
            String name = StringOps.toTrimOrNull(type.getAnnotation(Service.class).value())

            if (name)
            {
                return name
            }
        }
        type.getSimpleName().trim()
    }

    @Override
    public String getVersion()
    {
        Class<?> type = getClass()

        if (type.isAnnotationPresent(RPCVersion.class))
        {
            String version = StringOps.toTrimOrNull(type.getAnnotation(RPCVersion.class).value())

            if (StringOps.isVersionID(version))
            {
                return version
            }
        }
        '1.0'
    }

    @Override
    public JSONObject getValidation()
    {
        json([request: false, response: false])
    }

    @Override
    public JSONObject getCommandMetaData()
    {
        json([name: getName(), version: getVersion(), validation: getValidation(), request: getRequestSchema(), response: getResponseSchema()])
    }

    @Override
    public JSONSchema getRequestSchema()
    {
        schema([title: getName(), description: 'Request Schema for ' + getName(), type: 'object', properties: [:]])
    }

    @Override
    public JSONSchema getResponseSchema()
    {
        schema([title: getName(), description: 'Response Schema for ' + getName(), type: 'object', properties: [:]])
    }
}