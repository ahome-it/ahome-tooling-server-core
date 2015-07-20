/*
 * Copyright (c) 2014,2015 Ahome' Innovation Technologies. All rights reserved.
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

package com.ait.tooling.server.core.support.spring.testing;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.ait.tooling.common.api.java.util.StringOps;
import com.ait.tooling.server.core.support.spring.IServerContext;
import com.ait.tooling.server.core.support.spring.ServerContextInstance;

public interface IServerCoreTesting
{
    public static class TestingOps implements Serializable
    {
        private static final long serialVersionUID = -7046252747020669594L;

        public static final void setupServerCoreLogging() throws Exception
        {
            setupServerCoreLogging("classpath:testing-log4j.xml");
        }

        public static final void setupServerCoreLogging(final String location) throws Exception
        {
            Log4jConfigurer.initLogging(StringOps.requireTrimOrNull(location));
        }

        public static final void closeServerCoreLogging()
        {
            Log4jConfigurer.shutdownLogging();
        }

        public static void setRootLoggingLevel(final Level level)
        {
            LogManager.getRootLogger().setLevel(level);
        }

        public static final IServerContext setupServerCoreContext(final List<String> locations)
        {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(locations.toArray(new String[0]), false);

            ServerContextInstance instance = ServerContextInstance.getServerContextInstance();

            instance.setApplicationContext(context);

            context.refresh();

            return instance;
        }

        public static final void closeServerCoreContext()
        {
            ClassPathXmlApplicationContext context = (ClassPathXmlApplicationContext) ServerContextInstance.getServerContextInstance().getApplicationContext();

            context.close();

            ServerContextInstance.getServerContextInstance().setApplicationContext(null);
        }
    }
}
