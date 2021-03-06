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

package com.ait.tooling.server.core.logging;

import org.apache.log4j.Logger;

import com.ait.tooling.common.api.java.util.StringOps;

public final class MDC
{
    private static final Logger logger = Logger.getLogger(MDC.class);
    
    private MDC()
    {
    }

    public static final void clear()
    {
        try
        {
            org.apache.log4j.MDC.clear();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static final void put(final String key, final Object val)
    {
        StringOps.requireTrimOrNull(key);

        try
        {
            org.apache.log4j.MDC.put(key, val);
        }
        catch (Exception e)
        {
            logger.error("put()", e);
        }
    }

    public static final void remove(final String key)
    {
        StringOps.requireTrimOrNull(key);

        try
        {
            org.apache.log4j.MDC.remove(key);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static final Object get(final String key)
    {
        StringOps.requireTrimOrNull(key);

        try
        {
            return org.apache.log4j.MDC.get(key);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
