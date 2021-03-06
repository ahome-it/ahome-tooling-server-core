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

package com.ait.tooling.server.core.support.spring.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.ait.tooling.common.api.java.util.StringOps;
import com.ait.tooling.server.core.json.JSONObject;

import wslite.soap.SOAPClient;

public class CoreNetworkProvider implements ICoreNetworkProvider
{
    private String                                m_user_agent = HTTPHeaders.DEFAULT_USER_AGENT;

    private List<Integer>                         m_good_codes = new ArrayList<Integer>();

    private static final PathParameters           EMPTY_PARAMS = new PathParameters();

    private static final CoreResponseErrorHandler NO_ERRORS_CB = new CoreResponseErrorHandler();

    private static final class CoreResponseErrorHandler implements ResponseErrorHandler
    {
        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException
        {
            return false;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException
        {
        }
    }

    public CoreNetworkProvider()
    {
        for (int i = 200; i < 300; i++)
        {
            m_good_codes.add(i);
        }
    }

    @Override
    public void close() throws IOException
    {
    }

    @Override
    public IRESTResponse get(final String path)
    {
        return exec(path, HttpMethod.GET, null, null, null);
    }

    @Override
    public IRESTResponse get(final String path, final HTTPHeaders headers)
    {
        return exec(path, HttpMethod.GET, null, null, headers);
    }

    @Override
    public IRESTResponse get(final String path, final PathParameters params)
    {
        return exec(path, HttpMethod.GET, null, params, null);
    }

    @Override
    public IRESTResponse get(final String path, final PathParameters params, final HTTPHeaders headers)
    {
        return exec(path, HttpMethod.GET, null, params, headers);
    }

    @Override
    public IRESTResponse put(final String path, final JSONObject body)
    {
        return exec(path, HttpMethod.PUT, body, null, null);
    }

    @Override
    public IRESTResponse put(final String path, final JSONObject body, final HTTPHeaders headers)
    {
        return exec(path, HttpMethod.PUT, body, null, headers);
    }

    @Override
    public IRESTResponse put(final String path, final JSONObject body, final PathParameters params)
    {
        return exec(path, HttpMethod.PUT, body, params, null);
    }

    @Override
    public IRESTResponse put(final String path, final JSONObject body, final PathParameters params, final HTTPHeaders headers)
    {
        return exec(path, HttpMethod.PUT, body, params, headers);
    }

    @Override
    public IRESTResponse post(final String path, final JSONObject body)
    {
        return exec(path, HttpMethod.POST, body, null, null);
    }

    @Override
    public IRESTResponse post(final String path, final JSONObject body, final HTTPHeaders headers)
    {
        return exec(path, HttpMethod.POST, body, null, headers);
    }

    @Override
    public IRESTResponse post(final String path, final JSONObject body, final PathParameters params)
    {
        return exec(path, HttpMethod.POST, body, params, null);
    }

    @Override
    public IRESTResponse post(final String path, final JSONObject body, final PathParameters params, final HTTPHeaders headers)
    {
        return exec(path, HttpMethod.POST, body, params, headers);
    }

    @Override
    public IRESTResponse patch(final String path, final JSONObject body)
    {
        return exec(path, HttpMethod.PATCH, body, null, null);
    }

    @Override
    public IRESTResponse patch(final String path, final JSONObject body, final HTTPHeaders headers)
    {
        return exec(path, HttpMethod.PATCH, body, null, headers);
    }

    @Override
    public IRESTResponse patch(final String path, final JSONObject body, final PathParameters params)
    {
        return exec(path, HttpMethod.PATCH, body, params, null);
    }

    @Override
    public IRESTResponse patch(final String path, final JSONObject body, final PathParameters params, final HTTPHeaders headers)
    {
        return exec(path, HttpMethod.PATCH, body, params, headers);
    }

    @Override
    public IRESTResponse delete(final String path)
    {
        return exec(path, HttpMethod.DELETE, null, null, null);
    }

    @Override
    public IRESTResponse delete(final String path, final HTTPHeaders headers)
    {
        return exec(path, HttpMethod.DELETE, null, null, headers);
    }

    @Override
    public IRESTResponse delete(final String path, final PathParameters params)
    {
        return exec(path, HttpMethod.DELETE, null, params, null);
    }

    @Override
    public IRESTResponse delete(final String path, final PathParameters params, final HTTPHeaders headers)
    {
        return exec(path, HttpMethod.DELETE, null, params, headers);
    }

    protected IRESTResponse exec(final String path, final HttpMethod method, final JSONObject body, PathParameters params, HTTPHeaders headers)
    {
        final String curl = StringOps.requireTrimOrNull(path);

        if (null == headers)
        {
            headers = new HTTPHeaders();
        }
        headers.doRESTHeaders(getDefaultUserAgent());

        final HttpEntity<String> entity = (null == body) ? new HttpEntity<String>(headers) : new HttpEntity<String>(body.toJSONString(), headers);

        if (null == params)
        {
            params = EMPTY_PARAMS;
        }
        final RestTemplate rest = new RestTemplate();

        rest.setErrorHandler(NO_ERRORS_CB);

        try
        {
            return new CoreRESTResponse(this, rest.exchange(curl, method, entity, String.class, params));
        }
        catch (Exception e)
        {
            return new CoreRESTResponse(this, UNKNOWN_ERROR, e.getMessage(), headers);
        }
    }

    @Override
    public ISOAPClient soap(final String path)
    {
        return new CoreSOAPClient(new SOAPClient(StringOps.requireTrimOrNull(path)));
    }

    @Override
    public String getDefaultUserAgent()
    {
        return m_user_agent;
    }

    @Override
    public void setDefaultUserAgent(final String agent)
    {
        m_user_agent = agent;
    }

    @Override
    public boolean isGoodCode(final int code)
    {
        return m_good_codes.contains(code);
    }

    @Override
    public void setGoodCodes(List<Integer> list)
    {
        m_good_codes.clear();

        m_good_codes.addAll(list);
    }
}
