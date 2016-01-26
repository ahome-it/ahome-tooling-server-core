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

import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.json.parser.JSONParser;
import com.ait.tooling.server.core.json.parser.JSONParserException;

public class CoreRESTTemplate implements IRESTTemplate
{
    private final RestTemplate m_template;

    public CoreRESTTemplate()
    {
        m_template = new RestTemplate();
    }

    @Override
    public IRESTResponse get(String path)
    {
        return new CoreRestResponse(m_template.exchange(path, HttpMethod.GET, null, String.class));
    }

    @Override
    public IRESTResponse get(String path, IHTTPHeaders headers)
    {
        return new CoreRestResponse(m_template.exchange(path, HttpMethod.GET, null, String.class));
    }

    @Override
    public IRESTResponse get(String path, Map<String, String> params)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IRESTResponse get(String path, Map<String, String> params, IHTTPHeaders headers)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IRESTResponse put(String path, JSONObject body)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IRESTResponse put(String path, IHTTPHeaders headers)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IRESTResponse put(String path, JSONObject body, IHTTPHeaders headers)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IRESTResponse post(String path, JSONObject body)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IRESTResponse post(String path, IHTTPHeaders headers)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IRESTResponse post(String path, JSONObject body, IHTTPHeaders headers)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IRESTResponse patch(String path, JSONObject body)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IRESTResponse patch(String path, IHTTPHeaders headers)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IRESTResponse patch(String path, JSONObject body, IHTTPHeaders headers)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IRESTResponse delete(String path, JSONObject body)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IRESTResponse delete(String path, IHTTPHeaders headers)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IRESTResponse delete(String path, JSONObject body, IHTTPHeaders headers)
    {
        // TODO Auto-generated method stub
        return null;
    }

    protected static class CoreRestResponse implements IRESTResponse
    {
        private final int    m_code;

        private final String m_body;

        public CoreRestResponse(final ResponseEntity<String> resp)
        {
            this(resp.getStatusCode().value(), resp.hasBody() ? resp.getBody() : "");
        }

        public CoreRestResponse(final int code, final String body)
        {
            m_code = code;

            m_body = body;
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
            try
            {
                return new JSONParser().parse(body());
            }
            catch (JSONParserException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }
}
