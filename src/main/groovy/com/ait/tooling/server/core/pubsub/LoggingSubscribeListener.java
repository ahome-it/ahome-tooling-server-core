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

package com.ait.tooling.server.core.pubsub;

import java.io.Serializable;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.ait.tooling.server.core.logging.ICoreLoggingOperations;

public class LoggingSubscribeListener implements IPubSubMessageReceivedHandler, ICoreLoggingOperations, Serializable
{
    private static final long serialVersionUID = 5154374919398530876L;

    private final Logger      m_logger         = Logger.getLogger(getClass());

    public LoggingSubscribeListener()
    {
        m_logger.setLevel(Level.DEBUG);
    }

    @Override
    public void onMessageReceived(final JSONMessage message)
    {
        if (null != message)
        {
            final Level level = getLoggingLevel();

            /*
             * Converting a JSONObject to a String MAY be an expensive operation if
             * logging is not enabled at this level.
             */

            if ((null != level) && (LogManager.getRootLogger().isEnabledFor(level)))
            {
                m_logger.log(level, message.toJSONString());
            }
        }
    }

    @Override
    public Level getLoggingLevel()
    {
        Level level = m_logger.getLevel();

        if (null != level)
        {
            return level;
        }
        m_logger.setLevel(Level.DEBUG);

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
        final Level level = getLoggingLevel();

        if (null != level)
        {
            return level.toString();
        }
        return "";
    }

    @Override
    public void setLoggingLevelAsString(String level)
    {
        setLoggingLevel(Level.toLevel(level));
    }
}
