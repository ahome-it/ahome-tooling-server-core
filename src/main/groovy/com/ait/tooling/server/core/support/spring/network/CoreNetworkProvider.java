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

package com.ait.tooling.server.core.support.spring.network;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.json.parser.JSONParser;
import com.ait.tooling.server.core.json.parser.JSONParserException;

public class CoreNetworkProvider implements ICoreNetworkProvider
{
    @Override
    public IRESTResponse get(String path)
    {
        return new CoreRestResponse(new RestTemplate().exchange(path, HttpMethod.GET, new HttpEntity<String>(new HTTPHeaders().doRESTHeaders()), String.class));
    }

    @Override
    public IRESTResponse get(String path, HTTPHeaders headers)
    {
        return new CoreRestResponse(new RestTemplate().exchange(path, HttpMethod.GET, new HttpEntity<String>(headers.doRESTHeaders()), String.class));
    }

    @Override
    public IRESTResponse get(String path, Map<String, ?> params)
    {
        return new CoreRestResponse(new RestTemplate().exchange(path, HttpMethod.GET, new HttpEntity<String>(new HTTPHeaders().doRESTHeaders()), String.class, params));
    }

    @Override
    public IRESTResponse get(String path, Map<String, ?> params, HTTPHeaders headers)
    {
        return new CoreRestResponse(new RestTemplate().exchange(path, HttpMethod.GET, new HttpEntity<String>(headers.doRESTHeaders()), String.class, params));
    }

    @Override
    public IRESTResponse put(String path, JSONObject body)
    {
        return new CoreRestResponse(new RestTemplate().exchange(path, HttpMethod.PUT, new HttpEntity<String>(body.toJSONString(), new HTTPHeaders().doRESTHeaders()), String.class));
    }

    @Override
    public IRESTResponse put(String path, JSONObject body, HTTPHeaders headers)
    {
        return new CoreRestResponse(new RestTemplate().exchange(path, HttpMethod.PUT, new HttpEntity<String>(body.toJSONString(), headers.doRESTHeaders()), String.class));
    }

    @Override
    public IRESTResponse post(String path, JSONObject body)
    {
        return new CoreRestResponse(new RestTemplate().exchange(path, HttpMethod.POST, new HttpEntity<String>(body.toJSONString(), new HTTPHeaders().doRESTHeaders()), String.class));
    }

    @Override
    public IRESTResponse post(String path, JSONObject body, HTTPHeaders headers)
    {
        return new CoreRestResponse(new RestTemplate().exchange(path, HttpMethod.POST, new HttpEntity<String>(body.toJSONString(), headers.doRESTHeaders()), String.class));
    }

    @Override
    public IRESTResponse patch(String path, JSONObject body)
    {
        return new CoreRestResponse(new RestTemplate().exchange(path, HttpMethod.PATCH, new HttpEntity<String>(body.toJSONString(), new HTTPHeaders().doRESTHeaders()), String.class));
    }

    @Override
    public IRESTResponse patch(String path, JSONObject body, HTTPHeaders headers)
    {
        return new CoreRestResponse(new RestTemplate().exchange(path, HttpMethod.PATCH, new HttpEntity<String>(body.toJSONString(), headers.doRESTHeaders()), String.class));
    }

    @Override
    public IRESTResponse delete(String path, JSONObject body)
    {
        return new CoreRestResponse(new RestTemplate().exchange(path, HttpMethod.DELETE, new HttpEntity<String>(body.toJSONString(), new HTTPHeaders().doRESTHeaders()), String.class));
    }

    @Override
    public IRESTResponse delete(String path, JSONObject body, HTTPHeaders headers)
    {
        return new CoreRestResponse(new RestTemplate().exchange(path, HttpMethod.DELETE, new HttpEntity<String>(body.toJSONString(), headers.doRESTHeaders()), String.class));
    }

    protected static class CoreRestResponse implements IRESTResponse
    {
        private final int         m_code;

        private final String      m_body;

        private final HTTPHeaders m_head;

        private JSONObject        m_json;

        public CoreRestResponse(final ResponseEntity<String> resp)
        {
            this(resp.getStatusCode().value(), (resp.hasBody() ? resp.getBody() : null), new HTTPHeaders(Collections.unmodifiableMap(resp.getHeaders())));
        }

        public CoreRestResponse(final int code, final String body, final HTTPHeaders head)
        {
            m_code = code;

            m_body = body;

            m_head = head;
        }

        @Override
        public int code()
        {
            return m_code;
        }

        @Override
        public String body()
        {
            return m_body;
        }

        @Override
        public JSONObject json()
        {
            if (null != m_json)
            {
                return m_json;
            }
            try
            {
                final String body = body();

                if ((null == body) || (body.isEmpty()))
                {
                    return null;
                }
                return (m_json = (new JSONParser().parse(body)));
            }
            catch (JSONParserException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public HTTPHeaders headers()
        {
            return m_head;
        }
    }
}
