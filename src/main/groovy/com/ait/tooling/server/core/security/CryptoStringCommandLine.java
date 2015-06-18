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

public final class CryptoStringCommandLine
{
    private CryptoStringCommandLine(final String[] args)
    {
        if (args.length != 4)
        {
            System.err.println("Incorrect args: -[ed] pass salt text");

            System.exit(1);
        }
        if ("-e".equals(args[0]))
        {
            AESStringCryptoProvider crpt = new AESStringCryptoProvider(args[1], args[2]);

            System.out.println(crpt.encrypt(args[3]));
        }
        else if ("-d".equals(args[0]))
        {
            AESStringCryptoProvider crpt = new AESStringCryptoProvider(args[1], args[2]);

            System.out.println(crpt.decrypt(args[3]));
        }
        else
        {
            System.err.println("Incorrect args: -[ed] pass salt text");

            System.exit(1);
        }
        System.exit(0);
    }

    public static final void main(final String[] args)
    {
        new CryptoStringCommandLine(args);
    }
}
