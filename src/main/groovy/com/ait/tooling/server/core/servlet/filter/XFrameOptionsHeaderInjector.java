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

package com.ait.tooling.server.core.servlet.filter;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ait.tooling.common.api.java.util.StringOps;
import com.ait.tooling.server.core.json.JSONObject;

public class XFrameOptionsHeaderInjector implements IHeaderInjector
{
    private static final List<String> PREFIXES  = Arrays.asList("DENY", "SAMEORIGIN", "ALLOW-FROM ");

    private String                    m_options = "DENY";

    public XFrameOptionsHeaderInjector()
    {
        this("DENY");
    }

    public XFrameOptionsHeaderInjector(final String options)
    {
        setOptions(options);
    }

    public String getOptions()
    {
        return m_options;
    }

    public XFrameOptionsHeaderInjector setOptions(final String options)
    {
        m_options = StringOps.toTrimOrNull(options);

        return this;
    }

    @Override
    public void inject(final HttpServletRequest request, final HttpServletResponse response)
    {
        final String options = StringOps.toTrimOrNull(getOptions());

        if (null != options)
        {
            for (String prefix : PREFIXES)
            {
                if (options.startsWith(prefix))
                {
                    response.setHeader(X_FRAME_OPTIONS_HEADER, options);

                    return;
                }
            }
        }
    }

    @Override
    public void config(final JSONObject config)
    {
        if (null != config)
        {
            setOptions(config.getAsString("options"));
        }
    }
}
