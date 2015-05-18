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

package com.ait.tooling.server.core.jmx.management;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import com.ait.tooling.common.api.java.util.StringOps;

@Component("CoreServerManager")
@ManagedResource(objectName = "com.ait.tooling.server.core.jmx.management:name=CoreServerManager", description = "Manage Server Operations.")
public class CoreServerManager implements ICoreServerManager
{
    private static final Logger                   logger             = Logger.getLogger(CoreServerManager.class);

    private boolean                               m_running          = true;

    private final ConcurrentHashMap<String, Long> m_operation_counts = new ConcurrentHashMap<String, Long>();

    public CoreServerManager()
    {
    }

    @Override
    public Level getLoggingLevel()
    {
        return LogManager.getRootLogger().getLevel();
    }

    @Override
    public void setLoggingLevel(final Level level)
    {
        if (null != level)
        {
            if (logger.isInfoEnabled())
            {
                logger.info("CoreServerManager setting logging level to " + level.toString());
            }
            else
            {
                System.err.println("[INFO] Disabled: CoreServerManager setting logging level to " + level.toString());
            }
            LogManager.getRootLogger().setLevel(level);
        }
        else
        {
            logger.error("ERROR: CoreServerManager setting logging level to null, level is " + getLoggingLevelByString());
        }
    }

    @ManagedAttribute(description = "Get global Log4j Level.")
    public final String getLoggingLevelByString()
    {
        return getLoggingLevel().toString();
    }

    @ManagedAttribute(description = "Set global Log4j Level.")
    public final void setLoggingLevelByString(final String level)
    {
        setLoggingLevel(Level.toLevel(StringOps.toTrimOrNull(level)));
    }

    @ManagedOperation(description = "Resume Server.")
    public void resume()
    {
        if (false == m_running)
        {
            m_running = true;

            logger.info("CoreServerManager.resume()");
        }
    }

    @ManagedOperation(description = "Suspend Server.")
    public void suspend()
    {
        if (true == m_running)
        {
            m_running = false;

            logger.info("CoreServerManager.suspend()");
        }
    }

    @Override
    @ManagedOperation(description = "Is Server Running.")
    public boolean isRunning()
    {
        return m_running;
    }

    @ManagedAttribute()
    public final ConcurrentHashMap<String, Long> getOperationCounts()
    {
        return m_operation_counts;
    }

    @Override
    public synchronized void doIncrementOperationCount(final String name)
    {
        if (null != name)
        {
            final Long count = m_operation_counts.get(name);

            if (null == count)
            {
                m_operation_counts.put(name, 1L);
            }
            else
            {
                m_operation_counts.put(name, count + 1L);
            }
        }
    }

    @Override
    public synchronized void doResetOperationCount(final String name)
    {
        if (null != name)
        {
            if (m_operation_counts.containsKey(name))
            {
                m_operation_counts.put(name, 0L);
            }
        }
    }
}