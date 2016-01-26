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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CoreNetworkProvider implements ICoreNetworkProvider
{
    private List<IHTTPProxy> m_proxies = new ArrayList<>(0);

    public CoreNetworkProvider()
    {
    }

    @Override
    public ICoreNetworkProvider setHTTPProxies(final IHTTPProxy... proxies)
    {
        return setHTTPProxies(Arrays.asList(proxies));
    }

    @Override
    public ICoreNetworkProvider setHTTPProxies(final List<IHTTPProxy> proxies)
    {
        m_proxies = new ArrayList<>(Objects.requireNonNull(proxies));

        return this;
    }

    @Override
    public IRESTTemplate getRESTTemplate()
    {
        return null;
    }

    @Override
    public IRESTTemplate getRESTTemplate(IHTTPConnectionFactory factory)
    {
        return null;
    }

    @Override
    public List<IHTTPProxy> getHTTPProxies()
    {
        return Collections.unmodifiableList(m_proxies);
    }

    @Override
    public ICoreNetworkProvider addHTTPProxy(final IHTTPProxy proxy)
    {
        if (false == m_proxies.contains(Objects.requireNonNull(proxy)))
        {
            m_proxies.add(proxy);
        }
        return this;
    }

    @Override
    public ICoreNetworkProvider removeHTTPProxy(final IHTTPProxy proxy)
    {
        if (m_proxies.contains(Objects.requireNonNull(proxy)))
        {
            m_proxies.remove(proxy);
        }
        return this;
    }
}
