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

package com.ait.tooling.server.core.security;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public abstract class AbstractStringCryptoProvider implements IStringCryptoProvider
{
    private final Logger m_logger = Logger.getLogger(getClass());

    protected AbstractStringCryptoProvider()
    {
        setLoggingLevel(Level.OFF);
    }

    protected Logger logger()
    {
        return m_logger;
    }

    @Override
    public Level getLoggingLevel()
    {
        return m_logger.getLevel();
    }

    @Override
    public void setLoggingLevel(final Level level)
    {
        if (null != level)
        {
            m_logger.setLevel(level);
        }
    }

    @Override
    public String getLoggingLevelAsString()
    {
        return getLoggingLevel().toString();
    }

    @Override
    public void setLoggingLevelAsString(final String level)
    {
        setLoggingLevel(Level.toLevel(level, Level.OFF));
    }
}
