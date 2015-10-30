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

package com.ait.tooling.server.core.support.spring.telemetry;

import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.Message;

import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.support.spring.IBuildDescriptor;

public interface ITelemetryProvider extends ITelemetryOps
{
    public boolean isSending();

    public void setSending(boolean sending);

    public boolean post(Message<JSONObject> message);

    public IBuildDescriptor getProductBuildDescriptor();
    
    public PublishSubscribeChannel getPublishSubscribeChannel();
    
    public Message<JSONObject> make(String action, JSONObject message);
}
