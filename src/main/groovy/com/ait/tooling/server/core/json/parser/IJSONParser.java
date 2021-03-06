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

package com.ait.tooling.server.core.json.parser;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import org.springframework.core.io.Resource;

import com.ait.tooling.server.core.json.JSONObject;
import com.ait.tooling.server.core.json.ParserException;

public interface IJSONParser
{
    public JSONObject parse(String in) throws ParserException;

    public JSONObject parse(InputStream in) throws ParserException;

    public JSONObject parse(Reader in) throws ParserException;

    public JSONObject parse(Resource in) throws ParserException;
    
    public JSONObject parse(File in) throws ParserException;
    
    public JSONObject parse(URL in) throws ParserException;
}
