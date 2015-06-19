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

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class AESCryptoPropertiesCommandLine
{
    private AESCryptoPropertiesCommandLine(final String[] args)
    {
        if (args.length != 4)
        {
            System.err.println("Incorrect args: -[ed] pass salt file");

            System.exit(1);
        }
        final Properties prop = new Properties();

        try
        {
            prop.load(new FileReader(args[3]));
        }
        catch (Exception e)
        {
            final InputStream istr = getClass().getClassLoader().getResourceAsStream(args[3]);

            if (null == istr)
            {
                System.err.println("Can't open file " + args[3]);

                System.exit(1);
            }
            try
            {
                prop.load(istr);
            }
            catch (IOException io)
            {
                System.err.println("Can't read file " + args[3]);

                io.printStackTrace();

                System.exit(1);
            }
        }
        if ("-e".equals(args[0]))
        {
            final AESStringCryptoProvider crpt = new AESStringCryptoProvider(args[1], args[2]);

            for (Object o : prop.keySet())
            {
                final String k = o.toString();

                final String v = prop.get(k).toString();

                System.out.println(k + "=" + crpt.encrypt(v));
            }
        }
        else if ("-d".equals(args[0]))
        {
            final AESStringCryptoProvider crpt = new AESStringCryptoProvider(args[1], args[2]);

            for (Object o : prop.keySet())
            {
                final String k = o.toString();

                final String v = prop.get(k).toString();

                System.out.println(k + "=" + crpt.decrypt(v));
            }
        }
        else
        {
            System.err.println("Incorrect args: -[ed] pass salt file");

            System.exit(1);
        }
        System.exit(0);
    }

    public static final void main(final String[] args)
    {
        new AESCryptoPropertiesCommandLine(args);
    }
}
