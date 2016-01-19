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

package com.ait.tooling.server.core.support.instrument.telemetry

import groovy.transform.CompileStatic

import java.lang.management.ManagementFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import org.springframework.messaging.Message

import com.ait.tooling.server.core.json.JSONObject
import com.ait.tooling.server.core.support.CoreGroovySupport

@CompileStatic
public class TelemetryProvider extends CoreGroovySupport implements ITelemetryProvider
{    
    private final List<String>             m_notags = []
    
    private boolean                        m_closed = false

    private boolean                        m_active = false
    
    private boolean                        m_islogs = false
    
    private final ExecutorService          m_expool

    public TelemetryProvider()
    {
        this(true)
    }

    public TelemetryProvider(final boolean active)
    {
        this(10)
    }

    public TelemetryProvider(final int pool)
    {
        this(true, pool)
    }
    
    public TelemetryProvider(final boolean active, final int pool)
    {
        this(active, pool, false)
    }

    public TelemetryProvider(final boolean active, final int pool, final boolean logs)
    {
        final int safe = Math.min(Math.max(pool, 1), 512)

        logger().info("TelemetryProvider(${active},${pool}:${safe})")
        
        m_islogs = logs

        m_active = active

        m_expool = Executors.newFixedThreadPool(safe)
    }

    @Override
    public boolean isActive()
    {
        m_active && (false == isClosed())
    }

    @Override
    public void close() throws IOException
    {
        logger().info('close()')

        if (false == m_closed)
        {
            m_closed = true

            m_expool.shutdownNow()
        }
    }

    @Override
    public boolean isClosed()
    {
        m_closed
    }

    public void health()
    {
        if (isActive())
        {
            broadcast('health', json(memory: getMemoryUsageObject()))

            logger().info('health()')
        }
    }

    @Override
    public JSONObject getHeapMemoryUsageObject()
    {
        binder().toJSONObject(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage())
    }

    @Override
    public JSONObject getOffHeapMemoryUsageObject()
    {
        binder().toJSONObject(ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage())
    }

    @Override
    public JSONObject getMemoryUsageObject()
    {
        json(heap: getHeapMemoryUsageObject(), off_heap: getOffHeapMemoryUsageObject())
    }

    protected Object convert(final Object object)
    {
        if (object)
        {
            if (object instanceof JSONObject)
            {
                return object
            }
            if (object instanceof Map)
            {
                return object
            }
            if (object instanceof List)
            {
                return object
            }
            if (object instanceof Collection)
            {
                return new ArrayList<Object>((Collection<?>) object)
            }
            if (object instanceof Message)
            {
                return convert(((Message<?>) object).getPayload())
            }
            if ((object as Object).getClass().isPrimitive())
            {
                return object
            }
            else
            {
                def json = binder().toJSONObject(object)

                if (json)
                {
                    return json
                }
            }
        }
        object
    }

    @Override
    public boolean broadcast(final String category, final Object message)
    {
        broadcast_message(category, m_notags, message)
    }

    @Override
    public boolean broadcast(final String category, final List<String> tags, final Object message)
    {
        broadcast_message(category, tags.unique(false), message)
    }

    private final boolean broadcast_message(final String category, final List<String> tags, final Object message)
    {
        if (isActive())
        {
            def time = System.currentTimeMillis()

            m_expool.submit {

                if (isActive())
                {
                    try
                    {
                        def send = new TelemetryMessage(category, tags, convert(message), time)

                        publish('TelemetryPublishMessageChannel', send.toJSONPublishMessage())

                        if (m_islogs)
                        {
                            logger().info(send.toJSONObject().toJSONString())
                        }
                        send.close()
                    }
                    catch (Exception e)
                    {
                        logger().error('broadcast()', e)
                    }
                }
            }
        }
        true
    }
}
