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

package com.ait.tooling.server.core.scripting;

import java.io.Closeable;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public interface IScriptingProvider extends Closeable
{
    public ScriptEngine getScriptEngine(ScriptType type);

    public ScriptEngine getScriptEngine(ScriptType type, ClassLoader loader);

    public List<String> getScriptingLanguageNames();

    public List<String> getScriptingLanguageNames(ClassLoader loader);

    public List<ScriptType> getScriptingLanguageTypes();

    public List<ScriptType> getScriptingLanguageTypes(ClassLoader loader);

    public ScriptEngineManager getScriptEngineManager();

    public ScriptEngineManager getScriptEngineManager(ClassLoader loader);
}
