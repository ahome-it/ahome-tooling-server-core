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

package com.ait.tooling.server.core.json.binder;

import java.util.List;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public final class YAMLBinder extends AbstractDataBinder
{
    public YAMLBinder()
    {
        super(new ObjectMapper(new YAMLFactory()));
    }

    public YAMLBinder(final MapperFeature... features)
    {
        super(new ObjectMapper(new YAMLFactory()), features);
    }

    public YAMLBinder(final List<MapperFeature> features)
    {
        super(new ObjectMapper(new YAMLFactory()), features);
    }

    @Override
    public BinderType getType()
    {
        return BinderType.YAML;
    }
}
