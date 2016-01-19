
package com.ait.tooling.server.core.test;

import java.io.IOException;
import java.io.Writer;

import com.ait.tooling.common.server.io.NoSyncBufferedWriter;
import com.ait.tooling.common.server.io.NoSyncStringBuilderWriter;
import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.json.parser.JSONParser;
import com.ait.tooling.server.core.json.parser.JSONParserException;
import com.fasterxml.jackson.databind.ObjectMapper;

import groovy.json.JsonParserType;
import groovy.json.JsonSlurper;

public class IOTest
{
    public IOTest()
    {
    }

    public static long w0(final int loop, final Writer w, final JSONObject json) throws IOException
    {
        final long time = System.currentTimeMillis();

        for (int i = 0; i < loop; i++)
        {
            json.writeJSONString(w);
        }
        return System.currentTimeMillis() - time;
    }

    public static long w1(final int loop, final Writer w, final JSONObject json) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();

        final long time = System.currentTimeMillis();

        for (int i = 0; i < loop; i++)
        {
            w.write(mapper.writeValueAsString(json));
        }
        return System.currentTimeMillis() - time;
    }

    public static long w2(final int loop, final Writer w, final JSONObject json) throws IOException
    {
        final long time = System.currentTimeMillis();

        for (int i = 0; i < loop; i++)
        {
            w.write(json.toJSONString(false));
        }
        return System.currentTimeMillis() - time;
    }

    public static long w3(final int loop, final Writer w, final JSONObject json) throws IOException
    {
        final NoSyncStringBuilderWriter out = new NoSyncStringBuilderWriter(1024 * 1024);

        final long time = System.currentTimeMillis();

        for (int i = 0; i < loop; i++)
        {
            json.writeJSONString(out);
        }
        w.write(out.toString());

        return System.currentTimeMillis() - time;
    }

    public static long w4(final int loop, final Writer w, final JSONObject json) throws IOException
    {
        final NoSyncBufferedWriter out = new NoSyncBufferedWriter(w);

        final long time = System.currentTimeMillis();

        for (int i = 0; i < loop; i++)
        {
            json.writeJSONString(out);
        }
        return System.currentTimeMillis() - time;
    }

    public static long r0(final int loop, final Writer w, final JSONObject json) throws JSONParserException
    {
        final String text = json.toJSONString(false);

        final JSONParser parser = new JSONParser();

        final long time = System.currentTimeMillis();

        for (int i = 0; i < loop; i++)
        {
            parser.parse(text);
        }
        return System.currentTimeMillis() - time;
    }

    public static long r1(final int loop, final Writer w, final JSONObject json) throws JSONParserException
    {
        final String text = json.toJSONString(false);

        final JsonSlurper slurper = new JsonSlurper().setCheckDates(false).setType(JsonParserType.INDEX_OVERLAY).setChop(true);
        

        System.out.println(slurper.parseText(text).getClass().getName());
        final long time = System.currentTimeMillis();

        for (int i = 0; i < loop; i++)
        {
            slurper.parseText(text);
        }
        return System.currentTimeMillis() - time;
    }
}
