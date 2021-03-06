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

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

public final class AESStringCryptoProvider implements IStringCryptoProvider
{
    private final TextEncryptor m_pcrypt;

    public AESStringCryptoProvider(final String pass, final String salt)
    {
        m_pcrypt = Encryptors.text(Objects.requireNonNull(pass), Objects.requireNonNull(salt));
    }

    @Override
    public final String encrypt(final String text)
    {
        return m_pcrypt.encrypt(Objects.requireNonNull(text));
    }

    @Override
    public final String decrypt(final String text)
    {
        return m_pcrypt.decrypt(Objects.requireNonNull(text));
    }
}
