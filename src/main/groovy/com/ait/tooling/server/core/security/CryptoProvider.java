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

import java.io.IOException;
import java.util.Objects;

public final class CryptoProvider implements ICryptoProvider
{
    private final AESStringCryptoProvider        m_pcrypt;

    private final SimpleBCryptHashProvider       m_bcrypt;

    private final SimpleSHA512HashProvider       m_hasher;

    private final SimpleKeyStringSigningProvider m_secret;

    private final SimpleCryptoKeysGenerator      m_keygen;

    public CryptoProvider(final String pass, final String salt)
    {
        this(pass, salt, pass, DEFAULT_STRENGTH);
    }

    public CryptoProvider(final String pass, final String salt, int strength)
    {
        this(pass, salt, pass, strength);
    }

    public CryptoProvider(final String pass, final String salt, final String sign)
    {
        this(pass, salt, sign, DEFAULT_STRENGTH);
    }

    public CryptoProvider(final String pass, final String salt, final String sign, int strength)
    {
        m_hasher = new SimpleSHA512HashProvider();

        m_bcrypt = new SimpleBCryptHashProvider(strength);

        m_secret = new SimpleKeyStringSigningProvider(Objects.requireNonNull(sign));

        m_pcrypt = new AESStringCryptoProvider(Objects.requireNonNull(pass), Objects.requireNonNull(salt));

        m_keygen = SimpleCryptoKeysGenerator.getCryptoKeysGenerator();
    }

    @Override
    public final String getRandomPass()
    {
        return m_keygen.getRandomPass();
    }

    @Override
    public final String getRandomSalt()
    {
        return m_keygen.getRandomSalt();
    }

    @Override
    public boolean isPassValid(final String pass)
    {
        return m_keygen.isPassValid(pass);
    }

    @Override
    public final String makeSignature(final String text)
    {
        return m_secret.makeSignature(Objects.requireNonNull(text));
    }

    @Override
    public final boolean testSignature(final String text, final String value)
    {
        return m_secret.testSignature(Objects.requireNonNull(text), Objects.requireNonNull(value));
    }

    @Override
    public final String makeBCrypt(final String text)
    {
        return m_bcrypt.makeBCrypt(Objects.requireNonNull(text));
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

    @Override
    public final boolean testBCrypt(final String text, final String value)
    {
        return m_bcrypt.testBCrypt(Objects.requireNonNull(text), Objects.requireNonNull(value));
    }

    @Override
    public String sha512(final String text, final String salt)
    {
        return m_hasher.sha512(Objects.requireNonNull(text), Objects.requireNonNull(salt));
    }

    @Override
    public String sha512(final String text, final String salt, final int iter)
    {
        return m_hasher.sha512(Objects.requireNonNull(text), Objects.requireNonNull(salt), iter);
    }

    @Override
    public String sha512(final String text)
    {
        return m_hasher.sha512(Objects.requireNonNull(text));
    }

    @Override
    public void close() throws IOException
    {
    }
}
