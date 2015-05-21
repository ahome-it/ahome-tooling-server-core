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

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.ait.tooling.common.api.java.util.StringOps;
import com.ait.tooling.server.core.support.spring.ServerContextInstance;

@SuppressWarnings("serial")
public abstract class AbstractPubSubDescriptor implements IPubSubDescriptor
{
    private String                                        m_name;

    private final PubSubChannelType                       m_type;

    private PubSubStateType                               m_state    = PubSubStateType.CLOSED;

    private IPubSubMessageHistoryDescriptor               m_hist;

    private final Logger                                  m_logger   = Logger.getLogger(getClass());

    private static final List<IPubSubMessageHistoryEntry> NO_HISTORY = Collections.unmodifiableList(new ArrayList<IPubSubMessageHistoryEntry>(0));

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

    protected void setState(final PubSubStateType state)
    {
        m_state = Objects.requireNonNull(state);
    }

    @Override
    public PubSubStateType getState()
    {
        return m_state;
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

    public void setMessageHistoryDescriptorName(final String name)
    {
        if ((null == name) || (name.isEmpty()))
        {
            return;
        }
        final ApplicationContext ctxt = ServerContextInstance.get().getApplicationContext();

        if (ctxt.containsBean(name))
        {
            try
            {
                final IPubSubMessageHistoryDescriptor hist = ctxt.getBean(name, IPubSubMessageHistoryDescriptor.class);

                if (null != hist)
                {
                    setMessageHistoryDescriptor(hist);
                }
                else
                {
                    logger().error("IPubSubMessageHistoryDescriptor(" + name + ") was null");
                }
            }
            catch (Exception e)
            {
                logger().error("Can't find IPubSubMessageHistoryDescriptor(" + name + ")", e);
            }
        }
        else
        {
            logger().error("Can't find IPubSubMessageHistoryDescriptor(" + name + ")");
        }
    }

    public void setMessageHistoryDescriptor(final IPubSubMessageHistoryDescriptor hist)
    {
        m_hist = hist;
    }

    protected IPubSubMessageHistoryDescriptor getPubSubMessageHistoryDescriptor()
    {
        return m_hist;
    }
}
