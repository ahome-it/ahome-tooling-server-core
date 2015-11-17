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

package com.ait.tooling.server.core.json.support

import com.ait.tooling.server.core.json.JSONArray
import com.ait.tooling.server.core.json.JSONObject
import com.ait.tooling.server.core.json.parser.JSONParser
import com.ait.tooling.server.core.json.parser.JSONParserException
import com.ait.tooling.server.core.json.schema.JSONSchema
import groovy.transform.CompileStatic


@CompileStatic
public trait JSONTrait
{
    public JSONObject json()
    {
        new JSONObject()
    }

    public  JSONObject json(final Map<String, ?> map)
    {
        new JSONObject(Objects.requireNonNull(map))
    }

    public JSONObject json(final String name, final Object value)
    {
        new JSONObject(Objects.requireNonNull(name), value)
    }

    public JSONObject json(final Collection<?> collection)
    {
        Objects.requireNonNull(collection)

        if (collection instanceof List)
        {
            json((List<?>) collection)
        }
        else if (collection instanceof Map)
        {
            json((Map<String, ?>) collection)
        }
        else
        {
            json(new ArrayList<Object>(collection))
        }
    }

    public JSONObject json(final List<?> list)
    {
        new JSONObject(Objects.requireNonNull(list))
    }

    public  JSONSchema jsonSchema(final Map<String, ?> schema)
    {
        JSONSchema.cast(json(Objects.requireNonNull(schema)))
    }

    public JSONObject jsonParse(final String string) throws JSONParserException
    {
        Objects.requireNonNull(string)

        final Object result = new JSONParser().parse(string)

        if ((null != result) && (result instanceof JSONObject))
        {
            ((JSONObject) result)
        }
        else
        {
            null
        }
    }

    public JSONObject jsonParse(final Reader reader) throws IOException, JSONParserException
    {
        Objects.requireNonNull(reader)

        final Object result = new JSONParser().parse(reader)

        if ((null != result) && (result instanceof JSONObject))
        {
            ((JSONObject) result)
        }
        else
        {
            null
        }
    }

    public JSONArray jarr()
    {
        new JSONArray()
    }

    public JSONArray jarr(final JSONObject object)
    {
        Objects.requireNonNull(object)

        final JSONArray list = jarr()

        jarr().add(object)

        list
    }

    public JSONArray jarr(final List<?> list)
    {
        new JSONArray(Objects.requireNonNull(list))
    }

    public JSONArray jarr(final Map<String, ?> map)
    {
        jarr(new JSONObject(Objects.requireNonNull(map)))
    }

    public JSONArray jarr(final String name, final Object value)
    {
        jarr(new JSONObject(Objects.requireNonNull(name), value))
    }

    public JSONArray jarr(final Collection<?> collection)
    {
        Objects.requireNonNull(collection)

        if (collection instanceof List)
        {
            jarr((List<?>) collection)
        }
        else if (collection instanceof Map)
        {
            jarr((Map<String, ?>) collection);
        }
        else
        {
            jarr(new ArrayList<Object>(collection))
        }
    }
}
