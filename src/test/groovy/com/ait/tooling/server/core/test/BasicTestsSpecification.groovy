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

package com.ait.tooling.server.core.test

import javax.script.ScriptEngine

import com.ait.tooling.server.core.json.JSONObject
import com.ait.tooling.server.core.json.binder.BinderType
import com.ait.tooling.server.core.json.support.JSONMapToTreeSolver
import com.ait.tooling.server.core.logging.MDC
import com.ait.tooling.server.core.logging.NanoTimer
import com.ait.tooling.server.core.scripting.ScriptType
import com.ait.tooling.server.core.support.CoreGroovyTrait
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
        def list = 1..100
        println list instanceof List

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
        def lang = scripting().getScriptingLanguageNames()
        println json(languages: lang)

        expect:
        "dean" == "dean"
    }

    def "test JS Script"()
    {
        setup:
        ScriptEngine engine = scripting().engine(ScriptType.JAVASCRIPT, reader('classpath:/com/ait/tooling/server/core/test/test.js'))
        println "JavaScript " + engine.get('x')
        engine.eval('increment_x()')
        println "JavaScript " + engine.get('x')

        expect:
        "dean" == "dean"
    }

    def "test Groovy Script"()
    {
        setup:
        ScriptEngine engine = scripting().engine(ScriptType.GROOVY, reader('classpath:/com/ait/tooling/server/core/test/test.gy'))
        println "Groovy " + engine.get('x')
        engine.eval('increment_x()')
        println "Groovy " + engine.get('x')

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
        def answ = resp.json()

        expect:
        true == resp.good()
        answ != null
        answ['id'] == 100
    }

    /*
    def "test Spring rest 2"()
    {
        setup:
        def resp = network().get('http://jsonplaceholder.typicode.com/posts/{id}', new PathParameters(id: 100))
        def code = resp.code()
        def answ = resp.json()

        expect:
        code == 200
        answ != null
        answ['id'] == 100
    }
*/
    
    def "test Spring rest 3"()
    {
        setup:
        def resp = network().post('http://jsonplaceholder.typicode.com/posts', json(data: [body: 'hi', value: 888]))
        def code = resp.code()
        def answ = resp.json()
        if (answ)
        {
            println "POST(" + answ.toString() + ")"
        }
        expect:
        true == resp.good()
        answ != null
        answ['id'] > 100
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
        binder().send(System.out, "Hi")
        println ""

        expect:
        text == valu
    }

    def "test yaml pojo 1"()
    {
        setup:
        BinderPOJO make = binder(BinderType.YAML).bind(resource('classpath:/com/ait/tooling/server/core/test/pojo.yml'), BinderPOJO)
        String valu = binder(BinderType.YAML).toString(make)
        println valu

        expect:
        valu == valu
    }

    def "test yaml pojo 2"()
    {
        setup:
        JSONObject json = binder(BinderType.YAML).bindJSON(resource('classpath:/com/ait/tooling/server/core/test/pojo.yml'))
        String valu = json.toJSONString()
        println valu

        expect:
        valu == valu
    }

    def "test yaml json 1"()
    {
        setup:
        JSONObject json = json(type: 'API', active: true, versions: [1, 2, 3, false], pojo: new BinderPOJO("Rosaria", 100), list:[])
        String valu = binder(BinderType.YAML).toString(json)
        println valu
        println json.toJSONString()

        expect:
        valu == valu
    }

    def "test yaml json 3"()
    {
        setup:
        JSONObject json = binder(BinderType.YAML).bindJSON(resource('classpath:/com/ait/tooling/server/core/test/test.yml'))
        String valu = json.toJSONString()
        println valu

        expect:
        valu == valu
    }

    def "test yaml pojo recycle"()
    {
        setup:
        BinderPOJO pojo = new BinderPOJO()
        pojo.setName('Dean S. Jones')
        pojo.setCost(9.99)
        String text = binder(BinderType.YAML).toString(pojo)
        BinderPOJO make = binder(BinderType.YAML).bind(text, BinderPOJO)
        String valu = binder(BinderType.YAML).toString(make)
        println text
        println valu

        expect:
        text == valu
    }
    
    def "test xml pojo recycle"()
    {
        setup:
        BinderPOJO pojo = new BinderPOJO()
        pojo.setName('Dean S. Jones')
        pojo.setCost(9.99)
        String text = binder(BinderType.XML).toString(pojo)
        BinderPOJO make = binder(BinderType.XML).bind(text, BinderPOJO)
        String valu = binder(BinderType.XML).toString(make)
        println text
        println valu

        expect:
        text == valu
    }

    def "test tree"()
    {
        setup:
        JSONMapToTreeSolver tree = new JSONMapToTreeSolver([[linked: 'tree', parent: 'level', column: 'children']]).setIncluded(['id', 'children'])
        tree << [id: '1', level: 1, tree: 0]
        tree << [id: '2', level: 2, tree: 1]
        tree << [id: '3', level: 3, tree: 2]
        JSONObject json = tree.solve('tree')
        String valu = json.toJSONString()
        println valu

        expect:
        valu == valu
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
    
    def "test JavaScript scripting Proxy"()
    {
        setup:
        def p = scripting().proxy(ScriptType.JAVASCRIPT, reader('classpath:/com/ait/tooling/server/core/test/test.js'))
        
        p.increment_x()
        p.testargs(5, 'dean')
        p.x = 5
        def z = p.x
        println z
        
        expect:
        z == 5
    }
    
    def "test Groovy scripting Proxy"()
    {
        setup:
        def p = scripting().proxy(ScriptType.GROOVY, reader('classpath:/com/ait/tooling/server/core/test/test.gy'))
        
        p.increment_x()
        p.testargs(5, 'dean')
        p.x = 5
        def z = p.x
        println z
        
        expect:
        z == 5
    }
    
    def "Nano Timer"()
    {
        setup:
        def t = new NanoTimer()
        println t
        
        expect:
        "dean" == "dean"
    }
    
    def "Nano Timer Delay"()
    {
        setup:
        def t = new NanoTimer()
        Thread.sleep(500)
        println t
        
        expect:
        "dean" == "dean"
    }
    
    def "Parse Tiger JSON"()
    {
        setup:
        def t = new NanoTimer()
        for(int i = 0; i < 5000; i++)
        {
            def r = reader('classpath:/com/ait/tooling/server/core/test/tiger.json')
            jsonParse(r)
            r.close()
        }
        println t.toString() + " Tiger JSON parsed"
        
        expect:
        "dean" == "dean"
    }
    
    def "Parse Tiger Binder"()
    {
        setup:
        def t = new NanoTimer()
        for(int i = 0; i < 5000; i++)
        {
            def r = reader('classpath:/com/ait/tooling/server/core/test/tiger.json')
            binder().bindJSON(r)
            r.close()
        }
        println t.toString() + " Tiger BINDER parsed"
        
        expect:
        "dean" == "dean"
    }
    
    def "Parse Lion JSON"()
    {
        setup:
        def t = new NanoTimer()
        for(int i = 0; i < 5000; i++)
        {
            def r = reader('classpath:/com/ait/tooling/server/core/test/lion.json')
            jsonParse(r)
            r.close()
        }
        println t.toString() + " Lion JSON parsed"
        
        expect:
        "dean" == "dean"
    }
    
    def "Parse Lion Binder"()
    {
        setup:
        def t = new NanoTimer()
        for(int i = 0; i < 5000; i++)
        {
            def r = reader('classpath:/com/ait/tooling/server/core/test/lion.json')
            binder().bindJSON(r)
            r.close()
        }
        println t.toString() + " Lion BINDER parsed"
        
        expect:
        "dean" == "dean"
    }
    
    def "Parse Tiny JSON"()
    {
        setup:
        def t = new NanoTimer()
        for(int i = 0; i < 5000; i++)
        {
            def r = resource('classpath:/com/ait/tooling/server/core/test/tiny.json')
            jsonParse(r)
        }
        println t.toString() + " Tiny JSON parsed"
        
        expect:
        "dean" == "dean"
    }
    
    def "Parse Tiny Binder"()
    {
        setup:
        def t = new NanoTimer()
        for(int i = 0; i < 5000; i++)
        {
            def r = resource('classpath:/com/ait/tooling/server/core/test/tiny.json')
            binder().bindJSON(r)
        }
        println t.toString() + " Tiny BINDER parsed"
        
        expect:
        "dean" == "dean"
    }
    
    def "Nano Timer Last"()
    {
        setup:
        def t = new NanoTimer()
        println t
        
        expect:
        "dean" == "dean"
    }
}
