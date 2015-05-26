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

import java.io.IOException;
import java.util.Objects;

import com.ait.tooling.json.JSONObject;

@SuppressWarnings("serial")
public abstract class AbstractEventPubSubDescriptor extends AbstractPubSubDescriptor
{
    protected AbstractEventPubSubDescriptor()
    {
        super(PubSubChannelType.EVENT);
    }

    @Override
    public JSONObject publish(final JSONObject message) throws Exception
    {
        dispatch(new MessageReceivedEvent(this, Objects.requireNonNull(message)));

        return message;
    }

    @Override
    public void close() throws IOException
    {
    }
}
