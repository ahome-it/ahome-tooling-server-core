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

import java.util.Objects;

import javax.jms.Queue;

@SuppressWarnings("serial")
public abstract class AbstractJmsTemplateQueuePublishDescriptor extends AbstractJmsTemplatePublishDescriptor implements IJmsTempleteQueuePublishDescriptor
{
    private Queue m_queue;

    protected AbstractJmsTemplateQueuePublishDescriptor(final String name)
    {
        super(Objects.requireNonNull(name), PubSubChannelType.QUEUE);
    }

    @Override
    public void setQueue(final Queue queue)
    {
        m_queue = Objects.requireNonNull(queue);
    }

    @Override
    public Queue getQueue()
    {
        return m_queue;
    }

    @Override
    public <T extends Queue> T getQueue(final Class<T> type)
    {
        if (null != m_queue)
        {
            if (Objects.requireNonNull(type).isAssignableFrom(m_queue.getClass()))
            {
                return type.cast(m_queue);
            }
            else
            {
                logger().error("Attempt to cast type [" + m_queue.getClass().getName() + "] to type [" + type.getName() + "], returning null");
            }
        }
        return null;
    }
}
