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

package com.ait.tooling.server.core.security.session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

import com.ait.tooling.common.api.java.util.StringOps;
import com.ait.tooling.common.api.types.Activatable;

@ManagedResource(objectName = "com.ait.tooling.server.core.security.session:name=ServerSessionRepositoryProvider", description = "Manage Server Session Repositories.")
public class ServerSessionRepositoryProvider extends Activatable implements IServerSessionRepositoryProvider, BeanFactoryAware
{
    private static final long                                     serialVersionUID = -4351465993846157349L;

    private static final Logger                                   logger           = Logger.getLogger(ServerSessionRepositoryProvider.class);

    private final LinkedHashMap<String, IServerSessionRepository> m_repositories   = new LinkedHashMap<String, IServerSessionRepository>();

    public ServerSessionRepositoryProvider()
    {
        setActive(true);
    }

    protected void addSessionRepository(final IServerSessionRepository repository)
    {
        if (null != repository)
        {
            final ISessionDomain domain = repository.getDomain();

            if (null != domain)
            {
                final String name = StringOps.toTrimOrNull(domain.getName());

                if (null != name)
                {
                    if (null == m_repositories.get(name))
                    {
                        m_repositories.put(name, repository);

                        logger.info("ServerSessionRepositoryProvider.addSessionRepository(" + name + ") Registered");
                    }
                    else
                    {
                        logger.error("ServerSessionRepositoryProvider.addSessionRepository(" + name + ") Duplicate ignored");
                    }
                }
                else
                {
                    logger.error("ServerSessionRepositoryProvider.addSessionRepository() null domain name");
                }
            }
            else
            {
                logger.error("ServerSessionRepositoryProvider.addSessionRepository() null domain");
            }
        }
        else
        {
            logger.error("ServerSessionRepositoryProvider.addSessionRepository() null repository");
        }
    }

    @Override
    @ManagedOperation(description = "Close Provider.")
    public void close() throws IOException
    {
        setActive(false);
    }

    @Override
    @ManagedOperation(description = "Is Provider Active.")
    public boolean isActive()
    {
        return super.isActive();
    }

    @Override
    @ManagedOperation(description = "Set Provider Active.")
    public boolean setActive(final boolean active)
    {
        return super.setActive(active);
    }

    @Override
    @ManagedOperation(description = "Get Domain Names.")
    public List<String> getServerSessionRepositoryDomainNames()
    {
        final HashSet<String> hset = new HashSet<String>();

        for (IServerSessionRepository repository : m_repositories.values())
        {
            if (null != repository)
            {
                final ISessionDomain domain = repository.getDomain();

                if (null != domain)
                {
                    final String name = domain.getName();

                    if (null != name)
                    {
                        hset.add(name);
                    }
                }
            }
        }
        return new ArrayList<String>(hset);
    }

    @Override
    public List<ISessionDomain> getServerSessionRepositoryDomains()
    {
        final ArrayList<ISessionDomain> list = new ArrayList<ISessionDomain>();

        for (IServerSessionRepository repository : m_repositories.values())
        {
            if (null != repository)
            {
                final ISessionDomain domain = repository.getDomain();

                if (null != domain)
                {
                    list.add(domain);
                }
            }
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public IServerSessionRepository getServerSessionRepository(final String domain_name)
    {
        final IServerSessionRepository repository = m_repositories.get(StringOps.requireTrimOrNull(domain_name));

        if (null != repository)
        {
            final ISessionDomain domain = repository.getDomain();

            if (null != domain)
            {
                if (domain_name.equals(domain.getName()))
                {
                    return repository;
                }
                else
                {
                    logger.error("ServerSessionRepositoryProvider.getServerSessionRepository(" + domain_name + ") does not match " + domain.getName());
                }
            }
            else
            {
                logger.error("ServerSessionRepositoryProvider.getServerSessionRepository(" + domain_name + ") null domain");
            }
        }
        return null;
    }

    @Override
    public void setBeanFactory(final BeanFactory factory) throws BeansException
    {
        if (factory instanceof DefaultListableBeanFactory)
        {
            for (IServerSessionRepository repository : ((DefaultListableBeanFactory) factory).getBeansOfType(IServerSessionRepository.class).values())
            {
                addSessionRepository(repository);
            }
        }
    }

    @Override
    @ManagedOperation(description = "Clean Expired Sessions.")
    public void cleanExpiredSessions()
    {
        for (IServerSessionRepository repository : m_repositories.values())
        {
            if (null != repository)
            {
                try
                {
                    repository.cleanExpiredSessions();
                }
                catch (Exception e)
                {
                    logger.error("ServerSessionRepositoryProvider.cleanExpiredSessions() error.", e);
                }
            }
        }
    }
}
