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

package com.ait.tooling.server.core.support.instrument.telemetry;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.springframework.messaging.Message;

import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.json.support.JSONUtilitiesInstance;

public class TelemetryProvider implements ITelemetryProvider {
	private static final long serialVersionUID = -3902901457710499217L;

	private static final Logger s_logger = Logger.getLogger(TelemetryProvider.class);

	private static final JSONUtilitiesInstance JSONUTIL = JSONUtilitiesInstance.getJSONUtilitiesInstance();

	private boolean m_closed = false;

	private final ExecutorService m_expool = Executors.newFixedThreadPool(25);

	public TelemetryProvider() {
		s_logger.info("TelemetryProvider()");
	}

	@Override
	public void close() throws IOException {
		s_logger.info("close()");

		if (false == m_closed) {
			m_closed = true;

			m_expool.shutdownNow();
		}
	}

	@Override
	public boolean isClosed() {
		return m_closed;
	}

	public void health() {
		broadcast("health", new JSONObject("memory", getMemoryUsageObject()));

		s_logger.info("health()");
	}

	private final JSONObject getHeapMemoryUsageObject() {
		return JSONUTIL.binder().toJSONObject(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage());
	}

	private final JSONObject getNonHeapMemoryUsageObject() {
		return JSONUTIL.binder().toJSONObject(ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage());
	}

	private final JSONObject getMemoryUsageObject() {
		return new JSONObject("heap", getHeapMemoryUsageObject()).set("non_heap", getNonHeapMemoryUsageObject());
	}

	private static final Object convert(final Object message) {
		if (null != message) {
			if (message instanceof JSONObject) {
				return message;
			}
			if (message instanceof Map) {
				return message;
			}
			if (message instanceof List) {
				return message;
			} else if (message instanceof Collection) {
				return new ArrayList<Object>((Collection<?>) message);
			} else if (message instanceof Message) {
				return convert(((Message<?>) message).getPayload());
			} else if (message.getClass().isPrimitive()) {
				return message;
			} else {
				JSONObject sendjson = JSONUTIL.binder().toJSONObject(message);

				if (null == sendjson) {
					return message;
				}
				return sendjson;
			}
		}
		return null;
	}

	@Override
	public TelemetryProvider broadcast(final String category, final Object message) {
		if (false == isClosed()) {
			final long timemark = System.currentTimeMillis();

			m_expool.submit(new Runnable() {
				@Override
				public void run() {
					if (false == isClosed()) {
						try {
							final ITelemetryMessage send = new TelemetryMessage(category, convert(message), timemark);

							s_logger.info(send.toJSONObject().toJSONString());

							send.close();
						} catch (Exception e) {
							s_logger.error("broadcast()", e);
						}
					}
				}
			});
		}
		return this;
	}
}
