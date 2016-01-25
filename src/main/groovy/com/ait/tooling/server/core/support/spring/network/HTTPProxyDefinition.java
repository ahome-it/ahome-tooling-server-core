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

package com.ait.tooling.server.core.support.spring.network;

import com.ait.tooling.common.api.java.util.StringOps;
import com.ait.tooling.server.core.json.JSONObject;

public final class HTTPProxyDefinition implements IHTTPProxy
{
    private final int    m_port;

    private final String m_host;

    private final String m_schm;

    public HTTPProxyDefinition(final String host, final int port, final String schm)
    {
        m_port = Math.max(port, 80);

        m_host = StringOps.requireTrimOrNull(host);

        m_schm = StringOps.requireTrimOrNull(schm);
    }

    @Override
    public int getPort()
    {
        return m_port;
    }

    @Override
    public String getHost()
    {
        return m_host;
    }

    @Override
    public String getScheme()
    {
        return m_schm;
    }

    @Override
    public String toJSONString()
    {
        return toJSONObject().toJSONString();
    }

    @Override
    public JSONObject toJSONObject()
    {
        return new JSONObject("host", getHost()).set("port", getPort()).set("scheme", getScheme());
    }

    @Override
    public String toString()
    {
        return toJSONString();
    }

    @Override
    public int hashCode()
    {
        return toString().hashCode();
    }

    @Override
    public boolean equals(final Object other)
    {
        if (this == other)
        {
            return true;
        }
        if (null == other)
        {
            return false;
        }
        if (other instanceof HTTPProxyDefinition)
        {
            return toString().equals(other.toString());
        }
        return false;
    }
}
