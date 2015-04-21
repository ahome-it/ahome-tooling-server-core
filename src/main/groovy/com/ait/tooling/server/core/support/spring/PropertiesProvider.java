/*
   Copyright (c) 2014,2015 Ahome' Innovation Technologies. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.ait.tooling.server.core.support.spring;

import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertiesProvider extends PropertyPlaceholderConfigurer implements IPropertiesProvider
{
    private static final Logger     logger       = Logger.getLogger(PropertiesProvider.class);

    private HashMap<String, String> m_properties = new HashMap<String, String>();

    public PropertiesProvider()
    {
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory factory, Properties properties) throws BeansException
    {
        super.processProperties(factory, properties);

        logger.info("Procesing Properties");

        for (Object pkey : properties.keySet())
        {
            String valu = properties.get(pkey).toString();

            if (null != valu)
            {
                m_properties.put(pkey.toString(), valu);

                logger.info(pkey.toString() + " length " + valu.length());
            }
            else
            {
                logger.error(pkey.toString() + " is null");
            }
        }
    }

    @Override
    public Collection<String> keys()
    {
        return m_properties.keySet();
    }

    @Override
    public String getPropertyByName(String pkey)
    {
        return m_properties.get(pkey);
    }

    @Override
    public String getPropertyByName(String pkey, String defval)
    {
        if (m_properties.containsKey(pkey))
        {
            return m_properties.get(pkey);
        }
        return defval;
    }
}
