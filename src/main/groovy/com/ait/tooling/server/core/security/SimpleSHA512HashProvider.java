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

import java.security.MessageDigest;
import java.util.Objects;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

import com.ait.tooling.common.api.hash.Hasher;
import com.ait.tooling.common.api.java.util.IHTTPConstants;

public final class SimpleSHA512HashProvider implements ISHA512HashProvider
{
    private static final Logger logger   = Logger.getLogger(SimpleSHA512HashProvider.class);

    private final Hasher        m_hasher = new Hasher(this);

    public SimpleSHA512HashProvider()
    {
    }

    @Override
    public String sha512(final String text)
    {
        Objects.requireNonNull(text);

        MessageDigest md;

        try
        {
            md = MessageDigest.getInstance("SHA-512");
        }
        catch (Exception e)
        {
            logger.error("No SHA-512 Algorithm ", e);

            throw new IllegalArgumentException(e);
        }
        byte[] bytes;

        try
        {
            bytes = text.getBytes(IHTTPConstants.CHARSET_UTF_8);
        }
        catch (Exception e)
        {
            logger.error("No " + IHTTPConstants.CHARSET_UTF_8 + " encoding ", e);

            throw new IllegalArgumentException(e);
        }
        md.update(bytes);

        return Hex.encodeHexString(md.digest());
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
}
