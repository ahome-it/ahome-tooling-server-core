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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

import com.ait.tooling.server.core.security.IStringCryptoProvider;

public final class CoreEncryptedPropertiesProviderPlaceholderConfigurer extends CorePropertiesProviderPlaceholderConfigurer
{
    private static final long serialVersionUID = 3571385011409361222L;

    public CoreEncryptedPropertiesProviderPlaceholderConfigurer(final IStringCryptoProvider crypto)
    {
        this(crypto, "OFF");
    }

    public CoreEncryptedPropertiesProviderPlaceholderConfigurer(final IStringCryptoProvider crypto, final String level)
    {
        setPropertiesPersister(new CoreEncryptionPropertiesPersister(Objects.requireNonNull(crypto), level));
    }

    private static final class CoreEncryptionPropertiesPersister extends DefaultPropertiesPersister implements PropertiesPersister
    {
        private static final Logger         logger = Logger.getLogger(CoreEncryptionPropertiesPersister.class);

        private final Level                 m_levels;

        private final IStringCryptoProvider m_crypto;

        public CoreEncryptionPropertiesPersister(final IStringCryptoProvider crypto, final String level)
        {
            m_crypto = Objects.requireNonNull(crypto);

            m_levels = Level.toLevel(level, Level.OFF);
        }

        @Override
        public void load(final Properties props, final InputStream is) throws IOException
        {
            super.load(props, is);

            decrypt(props);
        }

        @Override
        public void load(final Properties props, final Reader reader) throws IOException
        {
            super.load(props, reader);

            decrypt(props);
        }

        @Override
        public void store(final Properties props, final OutputStream os, final String header) throws IOException
        {
            encrypt(props);

            super.store(props, os, header);
        }

        @Override
        public void store(final Properties props, final Writer writer, final String header) throws IOException
        {
            encrypt(props);

            super.store(props, writer, header);
        }

        @Override
        public void loadFromXml(final Properties props, final InputStream is) throws IOException
        {
            super.loadFromXml(props, is);

            decrypt(props);
        }

        @Override
        public void storeToXml(final Properties props, final OutputStream os, String header) throws IOException
        {
            encrypt(props);

            super.storeToXml(props, os, header);
        }

        @Override
        public void storeToXml(final Properties props, final OutputStream os, final String header, final String encoding) throws IOException
        {
            encrypt(props);

            super.storeToXml(props, os, header, encoding);
        }

        private final void decrypt(final Properties props)
        {
            final HashMap<String, String> saved = new HashMap<String, String>(props.size());

            for (Object o : props.keySet())
            {
                final String k = o.toString();

                final String v = props.getProperty(k);

                if ((null != v) && (false == v.isEmpty()))
                {
                    logger.log(m_levels, "decrypt(name:" + k + ",prop-value;" + v + ")");

                    final String d = m_crypto.decrypt(v);

                    logger.log(m_levels, "decrypt(name:" + k + ",decrypted:" + d + ")");

                    saved.put(k, d);
                }
            }
            for (String k : saved.keySet())
            {
                props.setProperty(k, saved.get(k));
            }
        }

        private final void encrypt(final Properties props)
        {
            final HashMap<String, String> saved = new HashMap<String, String>(props.size());

            for (Object o : props.keySet())
            {
                final String k = o.toString();

                final String v = props.getProperty(k);

                if ((null != v) && (false == v.isEmpty()))
                {
                    logger.log(m_levels, "encrypt(name:" + k + ",prop-value;" + v + ")");

                    final String e = m_crypto.encrypt(v);

                    logger.log(m_levels, "encrypt(name:" + k + ",encrypted:" + e + ")");

                    saved.put(k, e);
                }
            }
            for (String k : saved.keySet())
            {
                props.setProperty(k, saved.get(k));
            }
        }
    }
}
