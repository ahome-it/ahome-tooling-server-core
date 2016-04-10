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

package com.ait.tooling.server.core.test

import javax.script.ScriptEngine

import com.ait.tooling.server.core.json.JSONObject
import com.ait.tooling.server.core.logging.MDC
import com.ait.tooling.server.core.scripting.ScriptType
import com.ait.tooling.server.core.support.CoreGroovyTrait
import com.ait.tooling.server.core.support.spring.network.PathParameters
import com.ait.tooling.server.core.support.spring.testing.IServerCoreTesting.TestingOps
import com.ait.tooling.server.core.support.spring.testing.spock.ServerCoreSpecification

class BasicTestsSpecification extends ServerCoreSpecification implements CoreGroovyTrait
{
    def setupSpec()
    {
        MDC.put('session', uuid() + "-GLOBAL")
        
        TestingOps.setupServerCoreDefault(["classpath:/com/ait/tooling/server/core/test/ApplicationContext.xml", "classpath:/com/ait/tooling/server/core/config/CoreApplicationContext.xml"])
    }

    def cleanupSpec()
    {
        TestingOps.closeServerCoreDefault()
    }

    def "test server context property provider"()
    {
        expect: getPropertyByName("core.server.events.keep.alive") == "30"
    }

    def "test server context crypto provider"()
    {
        setup:
        def text = getCryptoProvider().encrypt("ok")

        expect:
        getCryptoProvider().decrypt(text) != null
        getCryptoProvider().decrypt(text) == "ok"
        getCryptoProvider().decrypt(text) != text
    }

    def "test JSONObject"()
    {
        setup:
        def valu = json(name: "Dean")

        expect:
        valu['name'] == "Dean"
    }

    def "test JSONObject 2"()
    {
        setup:
        def valu = json(count: 1L)
        def text = valu as String
        println text

        expect:
        valu['count'] == 1L
    }

    def "test JSONObject 3"()
    {
        setup:
        def valu = json(count: 1L, name: "Dean", last: 1.5)
        println valu as String
        valu - ['name', 'last']
        println valu as String

        expect:
        valu['count'] == 1L
    }

    def "test Keys"()
    {
        setup:
        String pass = getCryptoProvider().getRandomPass()
        String salt = getCryptoProvider().getRandomSalt()
        println pass
        println salt

        expect:
        getCryptoProvider().isPassValid(pass) == true
    }
    
    def "test script types"()
    {
        setup:
        def lang = getScriptingLanguageNames()
        println json(languages: lang)

        expect:
        "dean" == "dean"
    }
    
    def "test JS Script"()
    {
        setup:
        Reader rsrc = reader('classpath:/com/ait/tooling/server/core/test/test.js')
        ScriptEngine engine = scripting(ScriptType.JAVASCRIPT)
        engine.eval(rsrc)
        rsrc.close()
        println "JavaScript " + engine.get('x')
        engine.eval('increment_x()')
        println "JavaScript " + engine.get('x')
       
        expect:
        "dean" == "dean"
    }
    
    def "test Python Script"()
    {
        setup:
        Reader rsrc = reader('classpath:/com/ait/tooling/server/core/test/test.py')
        ScriptEngine engine = scripting(ScriptType.PYTHON)
        engine.eval(rsrc)
        rsrc.close()
        println "Python " + engine.get('x')
        engine.eval('increment_x()')
        println "Python " + engine.get('x')
       
        expect:
        "dean" == "dean"
    }
    
    def "test Groovy Script"()
    {
        setup:
        Reader rsrc = reader('classpath:/com/ait/tooling/server/core/test/test.gy')
        ScriptEngine engine = scripting(ScriptType.GROOVY)
        engine.eval(rsrc)
        rsrc.close()
        println "Groovy " + engine.get('x')
        engine.eval('increment_x()')
        println "Groovy " + engine.get('x')
       
        expect:
        "dean" == "dean"
    }
    
    def "test Ruby Script"()
    {
        setup:
        Reader rsrc = reader('classpath:/com/ait/tooling/server/core/test/test.rb')
        ScriptEngine engine = scripting(ScriptType.RUBY)
        engine.eval(rsrc)
        rsrc.close()
        println "Ruby " + engine.get('x')
        engine.eval('increment_x()')
        println "Ruby " + engine.get('x')
       
        expect:
        "dean" == "dean"
    }
    
    def "test Groovy soap"()
    {
        setup:
        def soap = network().soap('http://www.holidaywebservice.com/Holidays/US/Dates/USHolidayDates.asmx')       
        def resp = soap.send(SOAPAction: 'http://www.27seconds.com/Holidays/US/Dates/GetMothersDay') {
            body {
                GetMothersDay(xmlns: 'http://www.27seconds.com/Holidays/US/Dates/') {
                    year(2016)
                }
            }
        }
        def code = resp.code()
        def answ = resp.body().GetMothersDayResponse.GetMothersDayResult.text()
        println answ
        println resp.headers().toString()
        
        expect:
        code == 200
        answ == '2016-05-08T00:00:00'
    }
    
    def "test Spring rest 1"()
    {
        setup:
        def resp = network().get('http://jsonplaceholder.typicode.com/posts/100')
        def code = resp.code()
        def answ = resp.json()['id']
        println resp.json().toJSONString()
        
        expect:
        answ == 100
        code == 200
    }
    
    def "test Spring rest 2"()
    {
        setup:
        def resp = network().get('http://jsonplaceholder.typicode.com/posts/{id}', new PathParameters(id: 100))
        def code = resp.code()
        def answ = resp.json()['id']
        println resp.json().toJSONString()
        
        expect:
        answ == 100
        code == 200
    }

    def "test binder"()
    {
        setup:
        BinderPOJO pojo = new BinderPOJO()
        pojo.setName('Dean S. /Jones')
        String text = binder().toJSONString(pojo)
        BinderPOJO make = binder().bind(text, BinderPOJO)
        JSONObject json = binder().toJSONObject(make)
        String valu = json.toJSONString(false)
        pojo = json as BinderPOJO
        pojo.setName('Bob')
        println binder().toJSONString(pojo)
        println text
        println valu
        
        expect:
        text == valu
    }
    
    def "test MDC"()
    {
        setup:
        def keep = MDC.get('session')
        MDC.put('session', 'LOCAL')
        logger().info('MDC test')
        if (keep) {
            MDC.put('session', keep)
        }

        expect:
        "dean" == "dean"
    }
}
