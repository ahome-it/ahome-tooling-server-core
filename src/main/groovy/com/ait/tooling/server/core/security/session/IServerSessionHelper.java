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

package com.ait.tooling.server.core.security.session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.ait.tooling.common.api.java.util.StringOps;

public interface IServerSessionHelper
{
    public final static String SP_STATUS                           = "status";

    public final static String SP_DOMAIN                           = "domain";

    public final static String SP_ROLES                            = "roles";

    public final static String SP_USER_ID                          = "user_id";

    public final static String SP_SESSION_ID                       = "session_id";

    public final static String SP_PROXY_SESSION_ID                 = "proxy_session_id";

    public final static String SP_EXPIRED                          = "expired";

    public final static String SP_CREATION_TIME                    = "creation_time";

    public final static String SP_LAST_ACCESSED_TIME               = "last_accessed_time";

    public final static String SP_MAX_INACTIVE_INTERVAL_IN_SECONDS = "max_inactive_interval_in_seconds";

    default public String getStatusKey()
    {
        return SP_STATUS;
    }

    default public String getDomainKey()
    {
        return SP_DOMAIN;
    }

    default public String geRolesKey()
    {
        return SP_ROLES;
    }

    default public String getUserIdKey()
    {
        return SP_USER_ID;
    }

    default public String getSessionIdKey()
    {
        return SP_SESSION_ID;
    }

    default public String getProxySessionIdKey()
    {
        return SP_PROXY_SESSION_ID;
    }

    default public String getExpiredKey()
    {
        return SP_EXPIRED;
    }

    default public String getCreationTimeKey()
    {
        return SP_CREATION_TIME;
    }

    default public String getLastAccessedTimeKey()
    {
        return SP_LAST_ACCESSED_TIME;
    }

    default public String getMaxInactiveIntervalInSecondsKey()
    {
        return SP_MAX_INACTIVE_INTERVAL_IN_SECONDS;
    }

    default public List<String> toRolesList(final List<?> list)
    {
        if ((null != list) && (false == list.isEmpty()))
        {
            final HashSet<String> send = new HashSet<String>(list.size());

            for (Object elem : list)
            {
                if (elem instanceof String)
                {
                    final String valu = StringOps.toTrimOrNull(elem.toString());

                    if (null != valu)
                    {
                        send.add(valu);
                    }
                }
            }
            if (false == send.isEmpty())
            {
                return new ArrayList<String>(send);
            }
        }
        return null;
    }

    default public List<String> getDefaultRoles()
    {
        return Arrays.asList("ANONYMOUS");
    }
}
