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

package com.ait.tooling.server.core.support.spring;

import java.util.Objects;

import org.apache.log4j.Logger;

import com.ait.tooling.server.core.security.IStringCryptoProvider;

public final class CoreEncryptedPropertiesProviderPlaceholderConfigurer extends CorePropertiesProviderPlaceholderConfigurer
{
    private static final long           serialVersionUID = 3571385011409361222L;

    private static final Logger         logger           = Logger.getLogger(CoreEncryptedPropertiesProviderPlaceholderConfigurer.class);

    private final IStringCryptoProvider m_crypto;

    public CoreEncryptedPropertiesProviderPlaceholderConfigurer(final IStringCryptoProvider crypto)
    {
        m_crypto = Objects.requireNonNull(crypto);
    }

    @Override
    protected String convertProperty(final String name, final String value)
    {
        logger.info("convertProperty(name:" + name + ",value:" + value + ")");

        return convertPropertyValue(value);
    }

    @Override
    protected String convertPropertyValue(final String value)
    {
        final String found = super.convertPropertyValue(value);

        if (null == found)
        {
            return null;
        }
        logger.info("convertProperty(found:" + found + ",value:" + value + ")");

        final String crypt = m_crypto.decrypt(found);

        logger.info("convertProperty(crypt:" + crypt + ",value:" + value + ")");

        return crypt;
    }
}
