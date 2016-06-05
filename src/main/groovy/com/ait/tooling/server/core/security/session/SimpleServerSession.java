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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.ait.tooling.common.api.java.util.StringOps;
import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.json.JSONUtils;

public class SimpleServerSession implements IServerSession
{
    private boolean                        m_save;

    private final JSONObject               m_attr;

    private final IServerSessionRepository m_repo;

    public SimpleServerSession(final IServerSessionRepository repo)
    {
        m_attr = new JSONObject();

        m_repo = Objects.requireNonNull(repo);
    }

    public SimpleServerSession(final Map<String, ?> attr, final IServerSessionRepository repo)
    {
        m_attr = new JSONObject(Objects.requireNonNull(attr));

        m_repo = Objects.requireNonNull(repo);
    }

    @Override
    public long getCreationTime()
    {
        if (m_attr.isNumber(m_repo.getHelper().getCreationTimeKey()))
        {
            final Long lval = JSONUtils.asLong(m_attr.get(m_repo.getHelper().getCreationTimeKey()));

            if (null != lval)
            {
                return lval;
            }
        }
        return 0;
    }

    @Override
    public void setLastAccessedTime(final long time)
    {
        setAttribute(m_repo.getHelper().getLastAccessedTimeKey(), time);
    }

    @Override
    public long getLastAccessedTime()
    {
        if (m_attr.isNumber(m_repo.getHelper().getLastAccessedTimeKey()))
        {
            final Long lval = JSONUtils.asLong(m_attr.get(m_repo.getHelper().getLastAccessedTimeKey()));

            if (null != lval)
            {
                return lval;
            }
        }
        return 0;
    }

    @Override
    public void setMaxInactiveIntervalInSeconds(final int interval)
    {
        setAttribute(m_repo.getHelper().getMaxInactiveIntervalInSecondsKey(), interval);
    }

    @Override
    public int getMaxInactiveIntervalInSeconds()
    {
        if (m_attr.isInteger(m_repo.getHelper().getMaxInactiveIntervalInSecondsKey()))
        {
            return m_attr.getAsInteger(m_repo.getHelper().getMaxInactiveIntervalInSecondsKey());
        }
        return m_repo.getDefaultMaxInactiveIntervalInSeconds();
    }

    @Override
    public boolean isExpired()
    {
        if (m_attr.isBoolean(m_repo.getHelper().getExpiredKey()))
        {
            return m_attr.getAsBoolean(m_repo.getHelper().getExpiredKey());
        }
        return false;
    }

    @Override
    public String getId()
    {
        if (m_attr.isString(m_repo.getHelper().getSessionIdKey()))
        {
            return StringOps.toTrimOrNull(m_attr.getAsString(m_repo.getHelper().getSessionIdKey()));
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(final String name)
    {
        return (T) m_attr.get(Objects.requireNonNull(name));
    }

    @Override
    public Set<String> getAttributeNames()
    {
        return m_attr.keySet();
    }

    @Override
    public void setAttribute(final String name, final Object valu)
    {
        if (m_attr.isDefined(Objects.requireNonNull(name)))
        {
            final Object prev = m_attr.put(name, valu);

            if (null != prev)
            {
                if (false == prev.equals(valu))
                {
                    save();
                }
            }
        }
        else
        {
            m_attr.put(name, valu);

            save();
        }
    }

    @Override
    public void removeAttribute(final String name)
    {
        if (m_attr.isDefined(Objects.requireNonNull(name)))
        {
            m_attr.remove(name);

            save();
        }
    }

    @Override
    public String toJSONString()
    {
        return m_attr.toJSONString();
    }

    @Override
    public String getUserId()
    {
        if (m_attr.isString(m_repo.getHelper().getUserIdKey()))
        {
            return StringOps.toTrimOrNull(m_attr.getAsString(m_repo.getHelper().getUserIdKey()));
        }
        return null;
    }

    @Override
    public String getStatus()
    {
        if (m_attr.isString(m_repo.getHelper().getStatusKey()))
        {
            return StringOps.toTrimOrNull(m_attr.getAsString(m_repo.getHelper().getStatusKey()));
        }
        return null;
    }

    @Override
    public String getDomain()
    {
        if (m_attr.isString(m_repo.getHelper().getDomainKey()))
        {
            final String domain = StringOps.toTrimOrNull(m_attr.getAsString(m_repo.getHelper().getDomainKey()));

            if (null != domain)
            {
                return domain;
            }
        }
        return m_repo.getDomain();
    }

    @Override
    public List<String> getRoles()
    {
        if (m_attr.isArray(m_repo.getHelper().getDomainKey()))
        {
            final List<String> role = m_repo.getHelper().toRolesList(m_attr.getAsArray(m_repo.getHelper().getDomainKey()));

            if ((null != role) && (false == role.isEmpty()))
            {
                return role;
            }
        }
        final List<String> role = m_repo.getDefaultRoles();

        if ((null != role) && (false == role.isEmpty()))
        {
            return role;
        }
        return m_repo.getHelper().getDefaultRoles();
    }

    @Override
    public IServerSession getProxyForSession()
    {
        if (m_attr.isString(m_repo.getHelper().getProxySessionIdKey()))
        {
            final String proxy = StringOps.toTrimOrNull(m_attr.getAsString(m_repo.getHelper().getProxySessionIdKey()));

            if (null != proxy)
            {
                return m_repo.getSession(proxy);
            }
        }
        return null;
    }

    @Override
    public JSONObject toJSONObject()
    {
        return new JSONObject(m_attr);
    }

    @Override
    public boolean isPersisted()
    {
        return m_save;
    }

    @Override
    public void setPersisted(final boolean persisted)
    {
        m_save = persisted;
    }

    @Override
    public void save()
    {
        if (isPersisted())
        {
            m_repo.save(this);
        }
    }
}
