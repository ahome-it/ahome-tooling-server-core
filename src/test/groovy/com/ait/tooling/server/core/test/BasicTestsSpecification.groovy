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

import com.ait.tooling.server.core.json.JSONObject
import com.ait.tooling.server.core.support.CoreGroovyTrait
import com.ait.tooling.server.core.support.spring.testing.IServerCoreTesting.TestingOps
import com.ait.tooling.server.core.support.spring.testing.spock.ServerCoreSpecification

class BasicTestsSpecification extends ServerCoreSpecification implements CoreGroovyTrait
{
    def setupSpec()
    {
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

    def "test telemetry"()
    {
        setup:
        telemetry('testing', [x: 1])
        telemetry('testing', false)
        telemetry('testing', 5)
        telemetry('testing', ['Dean', 'S', 'Jones'])
        telemetry('testing', new BinderPOJO('Rosaria', 29.99))

        expect:
        "dean" == "dean"
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

    def "test chars"()
    {
        setup:
        def i = "11" as Integer
        println i + 2
        File file = new File("/tmp/chars")
        if (file.exists())
        {
            file.delete()
        }
        file.createNewFile()
        Writer writer = new BufferedWriter(new FileWriter(file))
        println "w0 " + IOTest.w0(10000000, writer, json(count: 1, name: "Dean", last: 1.5d, flag: false))
        println "w1 " + IOTest.w1(10000000, writer, json(count: 1, name: "Dean", last: 1.5d, flag: false))
        println "w2 " + IOTest.w2(10000000, writer, json(count: 1, name: "Dean", last: 1.5d, flag: false))
        println "w3 " + IOTest.w3(10000000, writer, json(count: 1, name: "Dean", last: 1.5d, flag: false))
        println "w4 " + IOTest.w4(10000000, writer, json(count: 1, name: "Dean", last: 1.5d, flag: false))
        println "r0 " + IOTest.r0(10000000, writer, json(count: 1, name: "Dean", last: 1.5d, flag: false))
        println "r1 " + IOTest.r1(10000000, writer, json(count: 1, name: "Dean", last: 1.5d, flag: false))
        
        expect:
        "dean" == "dean"
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
        //Thread.sleep(30000)

        expect:
        text == valu
    }
}
