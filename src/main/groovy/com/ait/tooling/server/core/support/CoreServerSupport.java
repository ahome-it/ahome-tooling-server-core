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

package com.ait.tooling.server.core.support;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.lang.StringEscapeUtils;

import com.ait.tooling.common.api.java.util.StringOps;

public class CoreServerSupport implements Serializable
{
    private static final long   serialVersionUID = -5881378494553257493L;

    private static final String OPEN             = "[";

    private static final String SHUT             = "]";

    private static final String SEPR             = ", ";

    private static final String QUOT             = "\"";

    private static final String NULL             = "null";

    public static final String toPrintString(final Collection<String> coll)
    {
        if (null == coll)
        {
            return NULL;
        }
        if (coll.isEmpty())
        {
            return OPEN + SHUT;
        }
        return toPrintString(StringOps.toArray(coll));
    }

    public static final String toPrintString(final String... list)
    {
        if (null == list)
        {
            return NULL;
        }
        if (list.length == 0)
        {
            return OPEN + SHUT;
        }
        final StringBuilder builder = new StringBuilder();

        builder.append(OPEN);

        for (String item : list)
        {
            if (null != item)
            {
                builder.append(QUOT).append(toEscapeJavaString(item)).append(QUOT);
            }
            else
            {
                builder.append(NULL);
            }
            builder.append(SEPR);
        }
        final int sepr = SEPR.length();

        final int leng = builder.length();

        final int tail = builder.lastIndexOf(SEPR);

        if ((tail >= 0) && (tail == (leng - sepr)))
        {
            builder.setLength(leng - sepr);
        }
        return builder.append(SHUT).toString();
    }

    public static final String toEscapeJavaString(final String value, boolean quot)
    {
        if (quot)
        {
            return QUOT + toEscapeJavaString(value) + QUOT;
        }
        else
        {
            return toEscapeJavaString(value);
        }
    }

    public static final String toEscapeJavaString(final String value)
    {
        return StringEscapeUtils.escapeJava(value);
    }

    protected CoreServerSupport()
    {
    }
}
