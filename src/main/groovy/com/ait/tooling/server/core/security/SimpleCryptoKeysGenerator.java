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

package com.ait.tooling.server.core.security;

import java.util.Objects;
import java.util.UUID;

public class SimpleCryptoKeysGenerator implements ICryptoKeysGenerator
{
    private static final long           serialVersionUID = 8605396298931990555L;

    private final IStringCryptoProvider m_crpt;

    public SimpleCryptoKeysGenerator(final IStringCryptoProvider crpt)
    {
        m_crpt = Objects.requireNonNull(crpt);
    }

    @Override
    public String getRandomPass()
    {
        return m_crpt.encrypt(getRandomUUID());
    }

    @Override
    public String getRandomSalt()
    {
        return m_crpt.encrypt(getRandomUUID());
    }

    @Override
    public String getRandomUUID()
    {
        return UUID.randomUUID().toString();
    }
}
