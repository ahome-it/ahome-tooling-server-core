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

package com.ait.tooling.server.core.pubsub.support

import groovy.transform.CompileStatic
import groovy.transform.Memoized

import com.ait.tooling.json.JSONObject
import com.ait.tooling.server.core.pubsub.IPubSubDescriptorProvider
import com.ait.tooling.server.core.pubsub.IPubSubHandlerRegistration
import com.ait.tooling.server.core.pubsub.IPubSubMessageReceivedHandler
import com.ait.tooling.server.core.pubsub.IPubSubProvider
import com.ait.tooling.server.core.pubsub.JSONMessage
import com.ait.tooling.server.core.pubsub.PubSubChannelType
import com.ait.tooling.server.core.support.CoreGroovySupport

@CompileStatic
public class PubSubSupport extends CoreGroovySupport implements IPubSubProvider
{
    @Memoized
    public IPubSubProvider getPubSubProvider()
    {
        getServerContext().getPubSubProvider()
    }

    @Memoized
    public IPubSubDescriptorProvider getPubSubDescriptorProvider()
    {
        getPubSubProvider().getPubSubDescriptorProvider()
    }

    @Override
    public void publish(String name, JSONMessage message) throws Exception
    {
        getPubSubProvider().publish(name, message)
    }

    @Override
    public void publish(String name, PubSubChannelType type, JSONMessage message) throws Exception
    {
        getPubSubProvider().publish(name, type, message)
    }

    @Override
    public void publish(String name, List<PubSubChannelType> list, JSONMessage message) throws Exception
    {
        getPubSubProvider().publish(name, list, message)
    }

    @Override
    public IPubSubHandlerRegistration addMessageReceivedHandler(String name, IPubSubMessageReceivedHandler handler) throws Exception
    {
        getPubSubProvider().addMessageReceivedHandler(name, handler)
    }

    @Override
    public IPubSubHandlerRegistration addMessageReceivedHandler(String name, PubSubChannelType type, IPubSubMessageReceivedHandler handler) throws Exception
    {
        getPubSubProvider().addMessageReceivedHandler(name, type, handler)
    }

    @Override
    public IPubSubHandlerRegistration addMessageReceivedHandler(String name, List<PubSubChannelType> list, IPubSubMessageReceivedHandler handler) throws Exception
    {
        getPubSubProvider().addMessageReceivedHandler(name, list, handler)
    }
}
