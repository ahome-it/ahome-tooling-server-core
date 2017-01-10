package com.ait.tooling.server.core.json.parser;

import java.io.InputStream;
import java.io.Reader;

import com.ait.tooling.common.server.io.AbstractProxyReader;

public class NoSyncProxyReader extends AbstractProxyReader
{
    public NoSyncProxyReader(Reader proxy)
    {
        super(proxy);
    }

    public NoSyncProxyReader(InputStream stream)
    {
        super(stream);
    }
}
