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

import com.ait.tooling.server.core.json.JSONObject

public class JSONMapToTreeSolver implements JSONTrait
{

    private List<String>    m_incl

    private List<String>    m_excl

    private List<String>    m_seen = []

    private List<Map>       m_rows = []

    private List<Map>       m_save = []

    private Map             m_spec = [:]

    public JSONMapToTreeSolver(Map spec)
    {
        this([Objects.requireNonNull(spec)])
    }

    public JSONMapToTreeSolver(List<Map> spec)
    {
        spec = Objects.requireNonNull(spec)

        spec.each { Map cols ->

            def parent = cols['parent'] as String

            if (null == m_spec[parent])
            {
                m_spec[parent] = [linked: cols['linked'] as String, column: cols['column'] as String, array: []]
            }
        }
    }

    public List<String> getIncluded()
    {
        m_incl
    }

    public JSONMapToTreeSolver setIncluded(List<String> incl)
    {
        m_incl = incl

        this
    }

    public List<String> getExcluded()
    {
        m_excl
    }

    public JSONMapToTreeSolver setExcluded(List<String> excl)
    {
        m_excl = excl

        this
    }

    public List<JSONObject> solve()
    {
        def list = []

        def incl = getIncluded()

        if ((incl) && (incl.size() < 1))
        {
            incl = null
        }
        def excl = getExcluded()

        if ((excl) && (excl.size() < 1))
        {
            excl = null
        }
        m_rows.each { Map jrow ->

            if (jrow)
            {
                if (incl)
                {
                    jrow = jrow.subMap(incl)
                }
                if (excl)
                {
                    jrow.keySet().removeAll(excl)
                }
                if (jrow.size() > 0)
                {
                    list << json(jrow)
                }
            }
        }
        m_rows.clear()

        list
    }

    public JSONObject solve(String name)
    {
        json(Objects.requireNonNull(name), solve())
    }

    public void add(Map jrow)
    {
        if (jrow)
        {
            m_rows << jrow
        }
    }

    public void leftShift(Map jrow)
    {
        if (jrow)
        {
            m_rows << jrow
        }
    }
}
