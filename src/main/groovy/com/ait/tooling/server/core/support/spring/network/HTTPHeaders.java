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

package com.ait.tooling.server.core.support.spring.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.ait.tooling.common.api.java.util.StringOps;
import com.ait.tooling.server.core.json.JSONObject;

public class HTTPHeaders extends HttpHeaders
{
    private static final long           serialVersionUID   = -7217466640722875327L;

    public static final String          DEFAULT_USER_AGENT = "Ahome-Tooling-Server-Core/1.1.X";

    public static final List<MediaType> JSON_MEDIA_TYPE    = Collections.unmodifiableList(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));

    HTTPHeaders(final HttpHeaders head)
    {
        addAllHeaders(head);
    }

    public HTTPHeaders()
    {
    }

    public HTTPHeaders(final Map<String, List<String>> head)
    {
        addAllHeaders(head);
    }

    public HTTPHeaders addAllHeaders(final Map<String, List<String>> head)
    {
        Objects.requireNonNull(head);

        if (false == head.isEmpty())
        {
            putAll(head);
        }
        return this;
    }

    public HTTPHeaders(final HttpServletRequest request)
    {
        final Enumeration<String> names = request.getHeaderNames();

        final HashMap<String, List<String>> head = new HashMap<String, List<String>>();

        while (names.hasMoreElements())
        {
            final String name = names.nextElement();

            if (null != name)
            {
                final Enumeration<String> vals = request.getHeaders(name);

                if (null != vals)
                {
                    final ArrayList<String> list = new ArrayList<String>(1);

                    while (vals.hasMoreElements())
                    {
                        final String valu = vals.nextElement();

                        if (null != valu)
                        {
                            list.add(valu);
                        }
                    }
                    if (false == list.isEmpty())
                    {
                        head.put(name, list);
                    }
                }
            }
        }
        addAllHeaders(head);
    }

    public HTTPHeaders setHttpServletResponse(final HttpServletResponse response)
    {
        for (String name : keySet())
        {
            for (String valu : get(name))
            {
                response.addHeader(name, valu);
            }
        }
        return this;
    }

    public HTTPHeaders setHttpServletResponse(final HttpServletResponse response, final Predicate<String> good)
    {
        for (String name : keySet())
        {
            if (good.test(name))
            {
                for (String valu : get(name))
                {
                    response.addHeader(name, valu);
                }
            }
        }
        return this;
    }

    public HTTPHeaders setHttpServletResponse(final HttpServletResponse response, final Collection<String> send, final Collection<String> dont)
    {
        for (String name : keySet())
        {
            if ((null != dont) && (dont.contains(name)))
            {
                continue;
            }
            if ((null == send) || (send.contains(name)))
            {
                for (String valu : get(name))
                {
                    response.addHeader(name, valu);
                }
            }
        }
        return this;
    }

    public HTTPHeaders doRESTHeaders()
    {
        return doRESTHeaders(DEFAULT_USER_AGENT);
    }

    public HTTPHeaders doRESTHeaders(final String ua)
    {
        final List<MediaType> list = getAccept();

        if ((null == list) || (list.isEmpty()))
        {
            setAccept(JSON_MEDIA_TYPE);
        }
        return doUserAgent(ua);
    }

    public HTTPHeaders doUserAgent()
    {
        return doUserAgent(DEFAULT_USER_AGENT);
    }

    public HTTPHeaders doUserAgent(final String ua)
    {
        return addUserAgent(StringOps.toTrimOrElse(ua, DEFAULT_USER_AGENT));
    }

    public HTTPHeaders addUserAgent(String ua)
    {
        ua = StringOps.toTrimOrNull(ua);

        if (null != ua)
        {
            final List<String> list = get(USER_AGENT);

            if (null == list)
            {
                add(USER_AGENT, ua);
            }
            else if (list.isEmpty())
            {
                add(USER_AGENT, ua);
            }
            else
            {
                for (String item : list)
                {
                    item = StringOps.toTrimOrNull(item);

                    if (null != item)
                    {
                        if (ua.equalsIgnoreCase(item))
                        {
                            return this;
                        }
                    }
                }
                add(USER_AGENT, ua);
            }
        }
        return this;
    }

    public JSONObject toJSONObject()
    {
        final Map<String, List<String>> make = new LinkedHashMap<String, List<String>>();

        for (String k : keySet())
        {
            make.put(k, get(k));
        }
        return new JSONObject(make);
    }

    public String toJSONString()
    {
        return toJSONObject().toJSONString();
    }

    @Override
    public String toString()
    {
        return toJSONString();
    }
}
