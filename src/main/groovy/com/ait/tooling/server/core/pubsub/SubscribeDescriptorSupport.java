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

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.ait.tooling.common.api.types.Activatable;

public class SubscribeDescriptorSupport extends Activatable implements Serializable, Closeable
{
    private static final long                                    serialVersionUID = -5741025269936370062L;

    private static final Logger                                  logger           = Logger.getLogger(SubscribeDescriptorSupport.class);

    private final AtomicLong                                     m_hkey           = new AtomicLong();

    private final LinkedHashMap<String, IMessageReceivedHandler> m_handlers       = new LinkedHashMap<String, IMessageReceivedHandler>();

    private final ExecutorService                                m_service        = Executors.newCachedThreadPool();

    public SubscribeDescriptorSupport()
    {
        super(true);
    }

    public void dispatch(final JSONMessage message, final ExecutorService service)
    {
        Objects.requireNonNull(message);

        Objects.requireNonNull(service);

        if (isActive())
        {
            for (IMessageReceivedHandler h : m_handlers.values())
            {
                final IMessageReceivedHandler handler = h;

                service.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            handler.onMessageReceived(message);
                        }
                        catch (Exception e)
                        {
                            logger.error("dispatch() error", e);
                        }
                    }
                });
            }
        }
        else
        {
            logger.error("dispatch is inactive " + message.toJSONString());
        }
    }

    public void dispatch(final JSONMessage message)
    {
        dispatch(Objects.requireNonNull(message), m_service);
    }

    public IMessageReceivedHandlerRegistration addMessageReceivedHandler(final IMessageReceivedHandler handler)
    {
        Objects.requireNonNull(handler);

        final String hkey = Long.toString(m_hkey.incrementAndGet());

        m_handlers.put(hkey, handler);

        return new IMessageReceivedHandlerRegistration()
        {
            private static final long serialVersionUID = -4801721135204751486L;

            @Override
            public void removeHandler()
            {
                m_handlers.remove(hkey);
            }
        };
    }

    @Override
    public void close() throws IOException
    {
        setActive(false);

        m_service.shutdown();
    }
}
