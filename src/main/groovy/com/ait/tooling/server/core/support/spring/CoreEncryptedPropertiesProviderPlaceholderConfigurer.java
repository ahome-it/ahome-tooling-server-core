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
import java.util.Objects;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

import com.ait.tooling.server.core.security.IStringCryptoProvider;

public final class CoreEncryptedPropertiesProviderPlaceholderConfigurer extends CorePropertiesProviderPlaceholderConfigurer
{
    private static final long serialVersionUID = 3571385011409361222L;

    public CoreEncryptedPropertiesProviderPlaceholderConfigurer(final IStringCryptoProvider crypto)
    {
        setPropertiesPersister(new CoreEncryptionPropertiesPersister(Objects.requireNonNull(crypto)));
    }

    private static final class CoreEncryptionPropertiesPersister extends DefaultPropertiesPersister implements PropertiesPersister
    {
        private static final Logger         logger = Logger.getLogger(CoreEncryptionPropertiesPersister.class);

        private final IStringCryptoProvider m_crypto;

        public CoreEncryptionPropertiesPersister(final IStringCryptoProvider crypto)
        {
            m_crypto = Objects.requireNonNull(crypto);
        }

        @Override
        public void load(Properties props, InputStream is) throws IOException
        {
            super.load(props, is);
        }

        @Override
        public void load(Properties props, Reader reader) throws IOException
        {
            super.load(props, reader);
        }

        @Override
        public void store(Properties props, OutputStream os, String header) throws IOException
        {
            super.store(props, os, header);
        }

        @Override
        public void store(Properties props, Writer writer, String header) throws IOException
        {
            super.store(props, writer, header);
        }

        @Override
        public void loadFromXml(Properties props, InputStream is) throws IOException
        {
            super.loadFromXml(props, is);
        }

        @Override
        public void storeToXml(Properties props, OutputStream os, String header) throws IOException
        {
            super.storeToXml(props, os, header);
        }

        @Override
        public void storeToXml(Properties props, OutputStream os, String header, String encoding) throws IOException
        {
            super.storeToXml(props, os, header, encoding);
        }
    }
}
