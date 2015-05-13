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

package com.ait.tooling.server.core.support.spring;

import java.util.Arrays;

import com.ait.tooling.common.api.java.util.IHTTPConstants;

public final class DefaultPrincipalsKeysProvider extends AbstractPrincipalsKeysProvider implements IHTTPConstants
{
    private static final long serialVersionUID = -620810050712727145L;

    public DefaultPrincipalsKeysProvider()
    {
        super(Arrays.asList(X_USER_ID_HEADER, X_SESSION_ID_HEADER, X_CLIENT_UUID_HEADER, X_SESSION_UUID_HEADER, X_XSRF_TOKEN_HEADER));
    }
}
