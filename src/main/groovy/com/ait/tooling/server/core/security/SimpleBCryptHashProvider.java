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

package com.ait.tooling.server.core.security;

import java.util.Objects;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class SimpleBCryptHashProvider implements IBCryptHashProvider
{
    private final BCryptPasswordEncoder m_bcrypt;

    public SimpleBCryptHashProvider()
    {
        m_bcrypt = new BCryptPasswordEncoder(DEFAULT_STRENGTH);
    }

    public SimpleBCryptHashProvider(final int strength)
    {
        m_bcrypt = new BCryptPasswordEncoder(strength);
    }

    @Override
    public final String makeBCrypt(final String text)
    {
        return m_bcrypt.encode(Objects.requireNonNull(text));
    }

    @Override
    public final synchronized boolean testBCrypt(final String text, final String value)
    {
        return m_bcrypt.matches(Objects.requireNonNull(text), Objects.requireNonNull(value));
    }
}
