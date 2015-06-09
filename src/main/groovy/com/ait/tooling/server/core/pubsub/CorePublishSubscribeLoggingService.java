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
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.ait.tooling.json.JSONObject;
import com.ait.tooling.server.core.logging.ICoreLoggingOperations;

@ManagedResource(objectName = "com.ait.tooling.server.core.pubsub:name=CorePublishSubscribeLoggingService", description = "Manage Publish Subscribe Logging Operations.")
public class CorePublishSubscribeLoggingService implements ICoreLoggingOperations, Serializable
{
    private static final long serialVersionUID = 5154374919398530876L;

    private final Logger      m_logger         = Logger.getLogger(getClass());

    public CorePublishSubscribeLoggingService(final PublishSubscribeChannel channel)
    {
        channel.subscribe(new MessageHandler()
        {
            @Override
            public void handleMessage(final Message<?> message) throws MessagingException
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
                        final Object payload = message.getPayload();

                        if (payload instanceof JSONObject)
                        {
                            m_logger.log(level, channel.getComponentName() + " " + payload.toString());
                        }
                    }
                }
            }
        });
        m_logger.setLevel(Level.INFO);
    }

    @Override
    public Level getLoggingLevel()
    {
        Level level = m_logger.getLevel();

        if (null != level)
        {
            return level;
        }
        m_logger.setLevel(Level.INFO);

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
    @ManagedAttribute(description = "Get Log4j Level.")
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
    @ManagedAttribute(description = "Set Log4j Level.")
    public void setLoggingLevelAsString(final String level)
    {
        setLoggingLevel(Level.toLevel(level, Level.INFO));
    }
}
