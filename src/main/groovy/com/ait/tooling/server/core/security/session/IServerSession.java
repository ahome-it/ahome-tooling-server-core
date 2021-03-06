/*
 * Copyright (c) 2017 Ahome' Innovation Technologies. All rights reserved.
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

package com.ait.tooling.server.core.security.session;

import java.io.Serializable;
import java.util.List;

import org.springframework.session.ExpiringSession;

import com.ait.tooling.common.api.json.JSONStringify;
import com.ait.tooling.server.core.json.JSONObject;

public interface IServerSession extends ExpiringSession, JSONStringify, Serializable
{
    public String getUserId();

    public String getStatus();

    public String getDomain();

    public List<String> getRoles();

    public IServerSession getProxyForSession();

    public JSONObject toJSONObject();

    public boolean isPersisted();

    public void setPersisted(boolean persisted);

    public void save();
    
    public void touch();
    
    public IServerSessionHelper getHelper();
}
