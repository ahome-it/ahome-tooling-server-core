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

import javax.jms.Topic;

@SuppressWarnings("serial")
public abstract class AbstractJmsTemplateTopicPublishDescriptor extends AbstractJmsTemplatePublishDescriptor implements IJmsTempleteTopicPublishDescriptor
{
    private Topic m_topic;

    protected AbstractJmsTemplateTopicPublishDescriptor(final String name)
    {
        super(Objects.requireNonNull(name));
    }

    @Override
    public void setTopic(final Topic topic)
    {
        m_topic = Objects.requireNonNull(topic);
    }

    @Override
    public Topic getTopic()
    {
        return m_topic;
    }

    @Override
    public <T extends Topic> T getTopic(final Class<T> type)
    {
        if (null != m_topic)
        {
            if (Objects.requireNonNull(type).isAssignableFrom(m_topic.getClass()))
            {
                return type.cast(m_topic);
            }
            else
            {
                logger().error("Attempt to cast type [" + m_topic.getClass().getName() + "] to type [" + type.getName() + "], returning null");
            }
        }
        return null;
    }
}
