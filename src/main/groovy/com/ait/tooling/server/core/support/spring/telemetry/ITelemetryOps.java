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

package com.ait.tooling.server.core.support.spring.telemetry;

import java.io.Closeable;
import java.io.Serializable;
import java.util.Objects;

import org.springframework.messaging.Message;

import com.ait.tooling.common.api.java.util.StringOps;
import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.support.spring.IBuildDescriptor;

public interface ITelemetryOps extends Closeable, Serializable
{
    public static final String TELEMETRY_ACTION_FIELD_NAME         = "TelemetryAction";

    public static final String TELEMETRY_MESSAGE_FIELD_NAME        = "TelemetryMessage";

    public static final String PRODUCT_BUILD_DESCRIPTOR_FIELD_NAME = "ProductBuildDescriptor";

    public static final class Builder implements Serializable
    {
        private static final long serialVersionUID = 8938977841815577782L;

        public static final JSONObject makeCoreTelemetryObject(final String action, final JSONObject message)
        {
            return new JSONObject(TELEMETRY_ACTION_FIELD_NAME, StringOps.requireTrimOrNull(action)).set(TELEMETRY_MESSAGE_FIELD_NAME, Objects.requireNonNull(message));
        }

        public static final Message<JSONObject> doEnsureTelemetryMessageForPublish(final Message<JSONObject> message, final IBuildDescriptor descriptor)
        {
            if (null != message)
            {
                final JSONObject payload = message.getPayload();

                if (null != payload)
                {
                    if (null != StringOps.toTrimOrNull(payload.getAsString(TELEMETRY_ACTION_FIELD_NAME)))
                    {
                        final JSONObject object = payload.getAsObject(PRODUCT_BUILD_DESCRIPTOR_FIELD_NAME);

                        if ((null == object) || (object.isEmpty()))
                        {
                            payload.put(PRODUCT_BUILD_DESCRIPTOR_FIELD_NAME, Objects.requireNonNull(Objects.requireNonNull(descriptor).toJSONObject()));
                        }
                        return message;
                    }
                }
            }
            return null;
        }

        private Builder()
        {
        }
    }
}
