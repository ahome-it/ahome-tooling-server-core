/*
   Copyright (c) 2014,2015 Ahome' Innovation Technologies. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.ait.tooling.server.core.rpc.commands

import groovy.transform.CompileStatic

import org.springframework.stereotype.Service

import com.ait.tooling.json.JSONObject
import com.ait.tooling.server.core.rpc.CoreGroovyCommandSupport
import com.ait.tooling.server.core.rpc.IJSONCommand
import com.ait.tooling.server.core.rpc.IJSONRequestContext
import com.ait.tooling.server.core.security.AuthorizationResult
import com.ait.tooling.server.core.security.Authorized
import com.google.common.util.concurrent.RateLimiter

@Service
@Authorized
@CompileStatic
public class DictionaryCommand extends CoreGroovyCommandSupport
{
    private final RateLimiter m_ratelimit = RateLimiter.create(0.1)

    @Override
    public JSONObject execute(IJSONRequestContext context, JSONObject object) throws Exception
    {
        m_ratelimit.acquire()

        List list = []

        JSONObject principals = context.getUserPrincipals()

        getCommandRegistry().getCommands().each { IJSONCommand command ->

            if (command)
            {
                AuthorizationResult auth = getAuthorizationProvider().isAuthorized(command, principals)

                if (auth.isAuthorized())
                {
                    list << command.getCommandMetaData()
                }
            }
        }
        json(list)
    }
}
