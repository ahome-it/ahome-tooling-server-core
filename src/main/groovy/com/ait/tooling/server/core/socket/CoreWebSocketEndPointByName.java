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

package com.ait.tooling.server.core.socket;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websockets/core/endpoints/{name}")
public class CoreWebSocketEndPointByName extends AbstractWebSocketEndPointByPathPart
{
    public CoreWebSocketEndPointByName()
    {
        super("name");
    }

    @OnOpen
    public void onOpen(final Session session)
    {
        super.onOpen(session);
    }

    @OnClose
    public void onClose(final Session session)
    {
        super.onClose(session);
    }

    @OnMessage
    public void onText(final Session session, final String text, final boolean last)
    {
        super.onText(session, text, last);
    }
}