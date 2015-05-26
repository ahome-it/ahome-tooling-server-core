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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.ait.tooling.common.api.java.util.StringOps;

@SuppressWarnings("serial")
public abstract class AbstractPubSubDescriptor implements IPubSubDescriptor
{
    private String                                                         m_name;

    private final PubSubChannelType                                        m_type;

    private IPubSubMessageHistoryDescriptor                                m_hist;

    private ExecutorService                                                m_exec                      = Executors.newFixedThreadPool(4);

    private final Logger                                                   m_logger                    = Logger.getLogger(getClass());

    private final AtomicLong                                               m_long                      = new AtomicLong();

    private final ConcurrentHashMap<String, IPubSubMessageReceivedHandler> m_message_received_handlers = new ConcurrentHashMap<String, IPubSubMessageReceivedHandler>();

    private static final List<IPubSubMessageHistoryEntry>                  NO_HISTORY                  = Collections.unmodifiableList(new ArrayList<IPubSubMessageHistoryEntry>(0));

    protected AbstractPubSubDescriptor(final PubSubChannelType type)
    {
        m_type = Objects.requireNonNull(type);
    }

    @Override
    public void setName(final String name)
    {
        m_name = StringOps.requireTrimOrNull(name);
    }

    @Override
    public String getName()
    {
        return m_name;
    }

    @Override
    public PubSubChannelType getChannelType()
    {
        return m_type;
    }

    @Override
    public List<IPubSubMessageHistoryEntry> history()
    {
        if (null == m_hist)
        {
            return NO_HISTORY;
        }
        return m_hist.history();
    }

    protected final Logger logger()
    {
        return m_logger;
    }

    public void setMessageHistoryDescriptor(final IPubSubMessageHistoryDescriptor hist)
    {
        m_hist = hist;
    }

    protected IPubSubMessageHistoryDescriptor getPubSubMessageHistoryDescriptor()
    {
        return m_hist;
    }

    protected void dispatch(final MessageReceivedEvent event) throws Exception
    {
        Objects.requireNonNull(event);

        record(event);

        for (IPubSubMessageReceivedHandler handler : m_message_received_handlers.values())
        {
            final PubSubNextEventActionType next = handler.onMesageReceived(event);

            if (PubSubNextEventActionType.CANCEL == next)
            {
                event.cancel();
            }
            if (event.isCancelled())
            {
                break;
            }
        }
    }

    protected void record(final MessageReceivedEvent event)
    {
        Objects.requireNonNull(event);

        final IPubSubMessageHistoryDescriptor hist = getPubSubMessageHistoryDescriptor();

        if (null == hist)
        {
            return;
        }
        m_exec.execute(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    hist.record(event);
                }
                catch (Exception e)
                {
                    logger().error("HistoryDescrptor(" + hist.getName() + ")", e);
                }
            }
        });
    }

    @Override
    public IPubSubHandlerRegistration addMessageReceivedHandler(final IPubSubMessageReceivedHandler handler)
    {
        Objects.requireNonNull(handler);

        final String hkey = Long.toString(m_long.incrementAndGet());

        m_message_received_handlers.put(hkey, handler);

        return new IPubSubHandlerRegistration()
        {
            @Override
            public void removeHandler()
            {
                m_message_received_handlers.remove(hkey);
            }
        };
    }
}
