/*
 * Copyright (c) 2014,2015,2016 Ahome' Innovation Technologies. All rights reserved.
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.util.ResourceUtils;

import com.ait.tooling.common.api.java.util.StringOps;
import com.ait.tooling.common.server.io.NoSyncStringBuilderWriter;

public final class PathResourceLoader implements Serializable
{
    private static final long   serialVersionUID = -294276736264556969L;

    private static final Logger logger           = Logger.getLogger(PathResourceLoader.class);

    private PathResourceLoader()
    {
    }

    public static final InputStream getPathResourceAsStream(String path)
    {
        path = StringOps.requireTrimOrNull(path);

        try
        {
            final URL url = ResourceUtils.getURL(path);

            if (ResourceUtils.URL_PROTOCOL_FILE.equals(url.getProtocol()))
            {
                final File file = ResourceUtils.getFile(url);

                if (false == file.exists())
                {
                    logger.error("path [" + path + "] does not exist.");

                    return null;
                }
                if (false == file.isFile())
                {
                    logger.error("path [" + path + "] is not a file.");

                    return null;
                }
                if (false == file.canRead())
                {
                    logger.error("path [" + path + "] is not readable.");

                    return null;
                }
                return new FileInputStream(file);
            }
            final URLConnection conn = url.openConnection();

            conn.setUseCaches(false);

            return conn.getInputStream();
        }
        catch (FileNotFoundException e)
        {
            logger.error("path [" + path + "] error.", e);

            return null;
        }
        catch (IOException e)
        {
            logger.error("path [" + path + "] error.", e);

            return null;
        }
    }

    public static final Reader getPathResourceAsReader(final String path)
    {
        final InputStream stream = getPathResourceAsStream(path);

        if (null != stream)
        {
            return new InputStreamReader(stream);
        }
        return null;
    }

    public static final String getPathResourceAsString(final String path)
    {
        final Reader read = getPathResourceAsReader(path);

        if (null == read)
        {
            return null;
        }
        final NoSyncStringBuilderWriter send = new NoSyncStringBuilderWriter();

        try
        {
            IOUtils.copy(IOUtils.toBufferedReader(read), send);

            return send.toString();
        }
        catch (IOException e)
        {
            logger.error("path [" + path + "] error.", e);
        }
        finally
        {
            IOUtils.closeQuietly(read);
        }
        return null;
    }
}
