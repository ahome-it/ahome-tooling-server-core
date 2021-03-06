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

import java.io.Closeable;
import java.io.Serializable;
import java.util.List;

import org.springframework.session.SessionRepository;

import com.ait.tooling.server.core.json.JSONObject;

public interface IServerSessionRepository extends SessionRepository<IServerSession>, Closeable, Serializable
{
    public boolean isActive();

    public String getDomain();

    public void save(IServerSession session);

    public void touch(String id);

    public void touch(IServerSession session);

    public void delete(String id);

    public void delete(IServerSession session);

    public void cleanExpiredSessions();

    public IServerSession createSession(JSONObject keys);

    public int getDefaultMaxInactiveIntervalInSeconds();

    public List<String> getDefaultRoles();
    
    public IServerSessionHelper getHelper();
}
