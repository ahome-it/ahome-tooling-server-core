/*
 * Copyright (c) 2014,2015 Ahome' Innovation Technologies. All rights reserved.
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

package com.ait.tooling.server.core.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.MDC;

import com.ait.tooling.common.api.java.util.IHTTPConstants;
import com.ait.tooling.common.api.java.util.StringOps;
import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.security.AuthorizationResult;
import com.ait.tooling.server.core.support.spring.IServerContext;
import com.ait.tooling.server.core.support.spring.ServerContextInstance;
import com.google.common.util.concurrent.RateLimiter;

@SuppressWarnings("serial")
public abstract class HTTPServletBase extends HttpServlet implements IHTTPConstants
{
    public static final String SESSION_DOMAIN_PARAM = "core.server.session.domain";

    public static final String UNKNOWN_USER         = "%-UNKNOWN-USER-%";

    public static final String NULL_SESSION         = "%-NULL_SESSION-%";

    public static final double MAX_RATE_LIMIT       = 2000.0;

    public static final double MIN_RATE_LIMIT       = 0.1000;

    private final RateLimiter  m_ratelimit;

    protected HTTPServletBase()
    {
        m_ratelimit = null;
    }

    protected HTTPServletBase(final double limit)
    {
        m_ratelimit = RateLimiter.create(Math.min(Math.max(limit, MIN_RATE_LIMIT), MAX_RATE_LIMIT));
    }

    protected final void ratelimit()
    {
        if (null != m_ratelimit)
        {
            m_ratelimit.acquire();
        }
    }

    protected String getDefaultSessionRepositoryDomain()
    {
        return getInitParameter(SESSION_DOMAIN_PARAM);
    }

    protected final static IServerContext getServerContext()
    {
        return ServerContextInstance.getServerContextInstance().getServerContext();
    }

    @Override
    public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
    {
        MDC.put("session", "");

        try
        {
            super.service(request, response);
        }
        catch (ServletException e)
        {
            throw e;
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            MDC.put("session", "");
        }
    }

    public static final LinkedHashMap<String, String> getParametersFromRequest(final HttpServletRequest request)
    {
        final LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();

        final Enumeration<String> names = request.getParameterNames();

        while (names.hasMoreElements())
        {
            final String name = names.nextElement();

            params.put(name, request.getParameter(name));
        }
        return params;
    }

    public static final JSONObject getJSONParametersFromRequest(final HttpServletRequest request)
    {
        return new JSONObject(getParametersFromRequest(request));
    }

    public static final LinkedHashMap<String, String> getHeadersFromRequest(final HttpServletRequest request)
    {
        final LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();

        final Enumeration<String> names = request.getHeaderNames();

        while (names.hasMoreElements())
        {
            final String name = names.nextElement();

            params.put(name, request.getHeader(name));
        }
        return params;
    }

    public static final JSONObject getJSONHeadersFromRequest(final HttpServletRequest request)
    {
        return new JSONObject(getHeadersFromRequest(request));
    }

    public static final JSONObject getUserPrincipalsFromRequest(final HttpServletRequest request, final List<String> keys)
    {
        final JSONObject principals = new JSONObject();

        for (String k : keys)
        {
            final String name = StringOps.toTrimOrNull(k);

            if (null != name)
            {
                String valu = request.getHeader(name);

                if (null != valu)
                {
                    principals.put(name, valu);
                }
                else
                {
                    valu = getServerContext().getPropertyByName(name);

                    if (null != valu)
                    {
                        principals.put(name, valu);
                    }
                }
            }
        }
        return principals;
    }

    protected final AuthorizationResult isAuthorized(final Object target, final List<String> roles)
    {
        return getServerContext().isAuthorized(target, roles);
    }
}
