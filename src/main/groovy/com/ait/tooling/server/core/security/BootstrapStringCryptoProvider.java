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
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import com.ait.tooling.common.api.java.util.StringOps;

public final class BootstrapStringCryptoProvider implements IStringCryptoProvider
{
    private static final Logger logger = Logger.getLogger(BootstrapStringCryptoProvider.class);

    private final TextEncryptor m_pcrypt;

    public BootstrapStringCryptoProvider(final Resource resource) throws Exception
    {
        this(resource, "bootstrap.crypto.pass", "bootstrap.crypto.salt");
    }

    public BootstrapStringCryptoProvider(final Resource resource, final String passname, final String saltname) throws Exception
    {
        logger.info("BootstrapStringCryptoProvider(" + resource.getURI().toString() + ", " + passname + ", " + saltname + ")");

        final Properties properties = new Properties();

        properties.load(resource.getInputStream());

        final String pass = StringOps.requireTrimOrNull(properties.getProperty(StringOps.requireTrimOrNull(passname)));

        final String salt = StringOps.requireTrimOrNull(properties.getProperty(StringOps.requireTrimOrNull(saltname)));

        if (SimpleCryptoKeysGenerator.getCryptoKeysGenerator().isPassValid(pass))
        {
            logger.info("BootstrapStringCryptoProvider(password has validated)");
        }
        else
        {
            logger.error("BootstrapStringCryptoProvider(password is not valid)");

            logger.trace("BootstrapStringCryptoProvider(password is not valid) " + pass);

            throw new IllegalArgumentException("BootstrapStringCryptoProvider(password is not valid)");
        }
        m_pcrypt = Encryptors.text(pass, salt);
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
