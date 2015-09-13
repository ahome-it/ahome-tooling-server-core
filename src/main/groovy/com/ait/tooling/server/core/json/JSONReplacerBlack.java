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

package com.ait.tooling.server.core.json;

import java.util.Collection;
import java.util.Objects;

public class JSONReplacerBlack extends AbstractJSONKeysObjectReplacer
{
    private static final long serialVersionUID = -1620524482987552561L;

    public JSONReplacerBlack()
    {
        super(false);
    }

    public JSONReplacerBlack(final Collection<String> keys)
    {
        super(false, Objects.requireNonNull(keys));
    }

    public JSONReplacerBlack(final String... keys)
    {
        super(false, keys);
    }
}
