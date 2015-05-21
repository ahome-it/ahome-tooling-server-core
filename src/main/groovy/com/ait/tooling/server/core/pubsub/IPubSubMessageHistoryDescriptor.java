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
import java.io.Serializable;
import java.util.List;

import com.ait.tooling.common.api.types.INamed;
import com.ait.tooling.json.JSONObject;

public interface IPubSubMessageHistoryDescriptor extends INamed, Closeable, Serializable
{
    public long getMaxSize();

    public void setMaxSize(long size);

    public long getMaxTime();

    public void setMaxTime(long time);

    public PubSubStateType getState();

    public PubSubChannelType getType();

    public void setState(PubSubStateType state);

    public void record(IPubSubEvent<JSONObject> event) throws Exception;

    public List<IPubSubMessageHistoryEntry> history();
}
