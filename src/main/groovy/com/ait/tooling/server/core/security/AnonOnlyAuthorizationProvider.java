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

package com.ait.tooling.server.core.security;

import java.io.IOException;

import com.ait.tooling.json.JSONObject;

public class AnonOnlyAuthorizationProvider implements IAuthorizationProvider
{
    @Override
    public void close() throws IOException
    {
    }

    @Override
    public AuthorizationResult isAuthorized(final Object target, final JSONObject principals)
    {
        if (null == target)
        {
            return new AuthorizationResult(false, false, E_SERVER_ERROR, "null");
        }
        if (null == principals)
        {
            return new AuthorizationResult(false, false, E_SERVER_ERROR, "null");
        }
        if (target instanceof IPrincipalsAuthorizer)
        {
            return ((IPrincipalsAuthorizer) target).isAuthorized(principals);
        }
        final Authorized authorized = target.getClass().getAnnotation(Authorized.class);

        if (null == authorized)
        {
            return new AuthorizationResult(true, false, E_IS_VALIDATED, "valid");
        }
        for (AuthorizationType type : authorized.value())
        {
            if (AuthorizationType.ANON != type)
            {
                return new AuthorizationResult(false, false, E_NO_ANONYMOUS, "anon");
            }
        }
        return new AuthorizationResult(true, false, E_IS_VALIDATED, "valid");
    }
}
