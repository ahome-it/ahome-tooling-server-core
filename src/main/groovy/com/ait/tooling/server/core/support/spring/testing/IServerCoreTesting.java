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
import java.util.Objects;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.ait.tooling.common.api.java.util.StringOps;
import com.ait.tooling.server.core.support.CoreServerSupport;
import com.ait.tooling.server.core.support.spring.IServerContext;
import com.ait.tooling.server.core.support.spring.ServerContextInstance;

public interface IServerCoreTesting
{
    public static class TestingOps implements Serializable
    {
        private static final long   serialVersionUID = -7046252747020669594L;

        private static final Logger logger           = Logger.getLogger("TestingOps");

        public static final void setupServerCoreLogging() throws Exception
        {
            setupServerCoreLogging("classpath:testing-log4j.xml");
        }

        public static final void setupServerCoreLogging(final String location) throws Exception
        {
            Log4jConfigurer.initLogging(Objects.requireNonNull(location));

            logger.info("setupServerCoreLogging(" + CoreServerSupport.toEscapeJavaString(location, true) + ")");
        }

        public static final void closeServerCoreLogging()
        {
            logger.info("closeServerCoreLogging()");

            Log4jConfigurer.shutdownLogging();
        }

        public static void setRootLoggingLevel(final Level level)
        {
            if (null != level)
            {
                logger.info("setRootLoggingLevel(" + level + ")");

                LogManager.getRootLogger().setLevel(level);
            }
            else
            {
                logger.error("setRootLoggingLevel(null)");
            }
        }

        public static final IServerContext setupServerCoreContext(final String... locations)
        {
            if (locations.length < 1)
            {
                logger.error("setupServerCoreContext() locations is empty.");
            }
            else
            {
                logger.info("setupServerCoreContext(" + CoreServerSupport.toPrintString(locations) + ")");
            }
            final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(locations, false);

            final ServerContextInstance instance = ServerContextInstance.getServerContextInstance();

            instance.setApplicationContext(context);

            context.refresh();

            return instance;
        }

        public static final void setupServerCoreDefault(final List<String> locations) throws Exception
        {
            setupServerCoreLogging();

            setupServerCoreContext(locations);
        }

        public static final IServerContext setupServerCoreContext(final List<String> locations)
        {
            return setupServerCoreContext(StringOps.toArray(locations));
        }

        public static final void closeServerCoreContext()
        {
            closeServerCoreContext(ServerContextInstance.getServerContextInstance());
        }

        public static final void closeServerCoreDefault()
        {
            closeServerCoreContext();

            closeServerCoreLogging();
        }

        public static final void closeServerCoreContext(final IServerContext instance)
        {
            final ClassPathXmlApplicationContext context = ((ClassPathXmlApplicationContext) instance.getApplicationContext());

            if (null != context)
            {
                if (context.isActive())
                {
                    context.close();
                }
                else
                {
                    logger.error("closeServerCoreContext() ApplicationContext is not active.");
                }
            }
            else
            {
                logger.error("closeServerCoreContext() ApplicationContext is null.");
            }
            ServerContextInstance.getServerContextInstance().setApplicationContext(null);
        }

        protected TestingOps()
        {
        }
    }
}
