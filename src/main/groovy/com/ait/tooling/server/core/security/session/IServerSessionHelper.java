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

package com.ait.tooling.server.core.security.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.ait.tooling.common.api.java.util.StringOps;

public interface IServerSessionHelper extends Serializable
{
    public final static String               SP_STATUS_KEY                           = "status";

    public final static String               SP_DOMAIN_KEY                           = "domain";

    public final static String               SP_ROLES_KEY                            = "roles";

    public final static String               SP_USER_ID_KEY                          = "user_id";

    public final static String               SP_SESSION_ID_KEY                       = "session_id";

    public final static String               SP_PROXY_SESSION_ID_KEY                 = "proxy_session_id";

    public final static String               SP_EXPIRED_KEY                          = "expired";

    public final static String               SP_CREATION_TIME_KEY                    = "creation_time";

    public final static String               SP_LAST_ACCESSED_TIME_KEY               = "last_accessed_time";

    public final static String               SP_MAX_INACTIVE_INTERVAL_IN_SECONDS_KEY = "max_inactive_interval_in_seconds";

    public final static String               SP_DEFAULT_DOMAIN                       = "default";

    public final static Integer              SP_MAX_INACTIVE_INTERVAL_IN_SECONDS     = 30 * 60;

    public final static List<String>         SP_DEFAULT_ROLES_LIST                   = Collections.unmodifiableList(Arrays.asList("ANONYMOUS"));

    public final static IServerSessionHelper SP_DEFAULT_HELPER_INSTANCE              = new IServerSessionHelper()
                                                                                     {
                                                                                         private static final long serialVersionUID = -3837786398300397591L;
                                                                                     };

    default public String getStatusKey()
    {
        return SP_STATUS_KEY;
    }

    default public String getDomainKey()
    {
        return SP_DOMAIN_KEY;
    }

    default public String geRolesKey()
    {
        return SP_ROLES_KEY;
    }

    default public String getUserIdKey()
    {
        return SP_USER_ID_KEY;
    }

    default public String getSessionIdKey()
    {
        return SP_SESSION_ID_KEY;
    }

    default public String getProxySessionIdKey()
    {
        return SP_PROXY_SESSION_ID_KEY;
    }

    default public String getExpiredKey()
    {
        return SP_EXPIRED_KEY;
    }

    default public String getCreationTimeKey()
    {
        return SP_CREATION_TIME_KEY;
    }

    default public String getLastAccessedTimeKey()
    {
        return SP_LAST_ACCESSED_TIME_KEY;
    }

    default public String getMaxInactiveIntervalInSecondsKey()
    {
        return SP_MAX_INACTIVE_INTERVAL_IN_SECONDS_KEY;
    }

    default public IServerSessionHelper getHelperInstance()
    {
        return SP_DEFAULT_HELPER_INSTANCE;
    }

    default public List<String> getDefaultRoles()
    {
        return SP_DEFAULT_ROLES_LIST;
    }

    default public int getDefaultMaxInactiveIntervalInSeconds()
    {
        return SP_MAX_INACTIVE_INTERVAL_IN_SECONDS;
    }

    default public String getDefaultDomain()
    {
        return SP_DEFAULT_DOMAIN;
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
                return Collections.unmodifiableList(new ArrayList<String>(send));
            }
        }
        return null;
    }
}
