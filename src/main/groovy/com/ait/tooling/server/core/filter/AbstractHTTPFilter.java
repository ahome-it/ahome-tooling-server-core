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

package com.ait.tooling.server.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ait.tooling.common.api.java.util.IHTTPConstants;

public abstract class AbstractHTTPFilter implements Filter, IHTTPConstants
{
    protected static final long WEEK_IN_SECONDS      = 604800L;

    protected static final long WEEK_IN_MILLISECONDS = 604800000L;

    protected static final long YEAR_IN_SECONDS      = 31536000L;

    protected static final long YEAR_IN_MILLISECONDS = 31536000000L;

    public abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException;

    @Override
    public void destroy()
    {
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException
    {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    @Override
    public void init(FilterConfig config) throws ServletException
    {
    }
}
