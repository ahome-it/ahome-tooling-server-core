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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HTTPHeaders extends HttpHeaders
{
    private static final long     serialVersionUID = -7217466640722875327L;

    public static List<MediaType> JSON_MEDIA_TYPE  = Arrays.asList(MediaType.APPLICATION_JSON);

    HTTPHeaders(HttpHeaders head)
    {
        putAll(head);
    }

    public HTTPHeaders()
    {
    }

    public HTTPHeaders(Map<String, List<String>> head)
    {
        putAll(head);
    }

    public HTTPHeaders doRESTHeaders()
    {
        List<MediaType> list = getAccept();

        if ((null == list) || (list.isEmpty()))
        {
            setAccept(JSON_MEDIA_TYPE);
        }
        return this;
    }
}
