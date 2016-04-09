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

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.ait.tooling.common.api.java.util.StringOps;
import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.json.parser.JSONParser;
import com.ait.tooling.server.core.json.parser.JSONParserException;

import wslite.soap.SOAPClient;

public class CoreNetworkProvider implements ICoreNetworkProvider
{
    public CoreNetworkProvider()
    {
    }

    @Override
    public void close() throws IOException
    {
    }

    @Override
    public IRESTResponse get(final String path)
    {
        return new CoreRESTResponse(new RestTemplate().exchange(StringOps.requireTrimOrNull(path), HttpMethod.GET, new HttpEntity<String>(new HTTPHeaders().doRESTHeaders()), String.class));
    }

    @Override
    public IRESTResponse get(final String path, final HTTPHeaders headers)
    {
        return new CoreRESTResponse(new RestTemplate().exchange(StringOps.requireTrimOrNull(path), HttpMethod.GET, new HttpEntity<String>(headers.doRESTHeaders()), String.class));
    }

    @Override
    public IRESTResponse get(final String path, final PathParameters params)
    {
        return new CoreRESTResponse(new RestTemplate().exchange(StringOps.requireTrimOrNull(path), HttpMethod.GET, new HttpEntity<String>(new HTTPHeaders().doRESTHeaders()), String.class, Objects.requireNonNull(params)));
    }

    @Override
    public IRESTResponse get(final String path, final PathParameters params, final HTTPHeaders headers)
    {
        return new CoreRESTResponse(new RestTemplate().exchange(StringOps.requireTrimOrNull(path), HttpMethod.GET, new HttpEntity<String>(headers.doRESTHeaders()), String.class, Objects.requireNonNull(params)));
    }
    
    @Override
    public IRESTResponse put(final String path, final JSONObject body)
    {
        return new CoreRESTResponse(new RestTemplate().exchange(StringOps.requireTrimOrNull(path), HttpMethod.PUT, new HttpEntity<String>(body.toJSONString(), new HTTPHeaders().doRESTHeaders()), String.class));
    }

    @Override
    public IRESTResponse put(final String path, final JSONObject body, final HTTPHeaders headers)
    {
        return new CoreRESTResponse(new RestTemplate().exchange(StringOps.requireTrimOrNull(path), HttpMethod.PUT, new HttpEntity<String>(body.toJSONString(), headers.doRESTHeaders()), String.class));
    }

    @Override
    public IRESTResponse put(final String path, final JSONObject body, final PathParameters params)
    {
        return new CoreRESTResponse(new RestTemplate().exchange(StringOps.requireTrimOrNull(path), HttpMethod.PUT, new HttpEntity<String>(body.toJSONString(), new HTTPHeaders().doRESTHeaders()), String.class, Objects.requireNonNull(params)));
    }

    @Override
    public IRESTResponse put(final String path, final JSONObject body, final PathParameters params, final HTTPHeaders headers)
    {
        return new CoreRESTResponse(new RestTemplate().exchange(StringOps.requireTrimOrNull(path), HttpMethod.PUT, new HttpEntity<String>(body.toJSONString(), headers.doRESTHeaders()), String.class, Objects.requireNonNull(params)));
    }

    @Override
    public IRESTResponse post(final String path, final JSONObject body)
    {
        return new CoreRESTResponse(new RestTemplate().exchange(StringOps.requireTrimOrNull(path), HttpMethod.POST, new HttpEntity<String>(body.toJSONString(), new HTTPHeaders().doRESTHeaders()), String.class));
    }

    @Override
    public IRESTResponse post(final String path, final JSONObject body, final HTTPHeaders headers)
    {
        return new CoreRESTResponse(new RestTemplate().exchange(StringOps.requireTrimOrNull(path), HttpMethod.POST, new HttpEntity<String>(body.toJSONString(), headers.doRESTHeaders()), String.class));
    }

    @Override
    public IRESTResponse post(final String path, final JSONObject body, final PathParameters params)
    {
        return new CoreRESTResponse(new RestTemplate().exchange(StringOps.requireTrimOrNull(path), HttpMethod.POST, new HttpEntity<String>(body.toJSONString(), new HTTPHeaders().doRESTHeaders()), String.class, Objects.requireNonNull(params)));
    }

    @Override
    public IRESTResponse post(final String path, final JSONObject body, final PathParameters params, final HTTPHeaders headers)
    {
        return new CoreRESTResponse(new RestTemplate().exchange(StringOps.requireTrimOrNull(path), HttpMethod.POST, new HttpEntity<String>(body.toJSONString(), headers.doRESTHeaders()), String.class, Objects.requireNonNull(params)));
    }
    
    @Override
    public IRESTResponse patch(final String path, final JSONObject body)
    {
        return new CoreRESTResponse(new RestTemplate().exchange(StringOps.requireTrimOrNull(path), HttpMethod.PATCH, new HttpEntity<String>(body.toJSONString(), new HTTPHeaders().doRESTHeaders()), String.class));
    }

    @Override
    public IRESTResponse patch(final String path, final JSONObject body, final HTTPHeaders headers)
    {
        return new CoreRESTResponse(new RestTemplate().exchange(StringOps.requireTrimOrNull(path), HttpMethod.PATCH, new HttpEntity<String>(body.toJSONString(), headers.doRESTHeaders()), String.class));
    }

    @Override
    public IRESTResponse patch(final String path, final JSONObject body, final PathParameters params)
    {
        return new CoreRESTResponse(new RestTemplate().exchange(StringOps.requireTrimOrNull(path), HttpMethod.PATCH, new HttpEntity<String>(body.toJSONString(), new HTTPHeaders().doRESTHeaders()), String.class, Objects.requireNonNull(params)));
    }

    @Override
    public IRESTResponse patch(final String path, final JSONObject body, final PathParameters params, final HTTPHeaders headers)
    {
        return new CoreRESTResponse(new RestTemplate().exchange(StringOps.requireTrimOrNull(path), HttpMethod.PATCH, new HttpEntity<String>(body.toJSONString(), headers.doRESTHeaders()), String.class, Objects.requireNonNull(params)));
    }
    
    @Override
    public IRESTResponse delete(final String path)
    {
        return new CoreRESTResponse(new RestTemplate().exchange(StringOps.requireTrimOrNull(path), HttpMethod.DELETE, new HttpEntity<String>(new HTTPHeaders().doRESTHeaders()), String.class));
    }

    @Override
    public IRESTResponse delete(final String path, final HTTPHeaders headers)
    {
        return new CoreRESTResponse(new RestTemplate().exchange(StringOps.requireTrimOrNull(path), HttpMethod.DELETE, new HttpEntity<String>(headers.doRESTHeaders()), String.class));
    }

    @Override
    public IRESTResponse delete(final String path, final PathParameters params)
    {
        return new CoreRESTResponse(new RestTemplate().exchange(StringOps.requireTrimOrNull(path), HttpMethod.DELETE, new HttpEntity<String>(new HTTPHeaders().doRESTHeaders()), String.class, Objects.requireNonNull(params)));
    }

    @Override
    public IRESTResponse delete(final String path, final PathParameters params, final HTTPHeaders headers)
    {
        return new CoreRESTResponse(new RestTemplate().exchange(StringOps.requireTrimOrNull(path), HttpMethod.DELETE, new HttpEntity<String>(headers.doRESTHeaders()), String.class, Objects.requireNonNull(params)));
    }

    @Override
    public ISOAPClient soap(final String path)
    {
        return new CoreSOAPClient(new SOAPClient(StringOps.requireTrimOrNull(path)));
    }
    
    protected static class CoreRESTResponse implements IRESTResponse
    {
        private static final Logger logger = Logger.getLogger(CoreRESTResponse.class);

        private final int           m_code;

        private final String        m_body;

        private final HTTPHeaders   m_head;

        private JSONObject          m_json;

        public CoreRESTResponse(final ResponseEntity<String> resp)
        {
            this(resp.getStatusCode().value(), (resp.hasBody() ? resp.getBody() : null), new HTTPHeaders(Collections.unmodifiableMap(resp.getHeaders())));
        }

        public CoreRESTResponse(final int code, final String body, final HTTPHeaders head)
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
        public synchronized JSONObject json()
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
                logger.error("Error parsing JSON", e);
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
