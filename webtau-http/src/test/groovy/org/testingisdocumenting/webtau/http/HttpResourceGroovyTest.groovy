/*
 * Copyright 2023 webtau maintainers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.webtau.http

import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.http.Http.*

class HttpResourceGroovyTest extends HttpTestBase {
    private final def priceNoParams = http.resource("/end-point").get("price")

    private final def nestedProp = http.resource("/end-point").object.k3

    // live-price-definition
    private final def livePrice = http.resource("/prices/:ticker").price
    // live-price-definition

    // no-path-definition
    private final def livePriceBody = http.resource("/prices/:ticker").body
    // no-path-definition

    // complex-path-definition
    private final def complexListFirstId = http.resource("/end-point").complexList[0].id
    // complex-path-definition

    // multiple-url-params-definition
    private final def myObj = http.resource("/end-point/:param1/:param2").object
    // multiple-url-params-definition

    @Test
    void "resource node no route param"() {
        priceNoParams.should == 100
    }

    @Test
    void "nested path no route params"() {
        nestedProp.should == "v3"
    }

    @Test
    void "wait for multiple times"() {
        HttpTestDataServer.IBM_PRICES.reset()

        // live-price-wait
        livePrice.of("IBM").waitToBe > 115
        // live-price-wait
    }

    @Test
    void "wait for multiple times no path"() {
        HttpTestDataServer.IBM_PRICES.reset()
        // no-path-wait
        livePriceBody.of("IBM").waitTo == [price: greaterThan(115)]
        // no-path-wait
    }

    @Test
    void "complex list first id"() {
        // complex-path-validation
        complexListFirstId.should == "id1"
        // complex-path-validation
    }

    @Test
    void "resource multiple params"() {
        // multiple-url-params-validation
        myObj.of([param1: 10, param2: 20]).should == [k1: "v1_", k2: "v2_", k3: "v3_"]
        // multiple-url-params-validation
    }

    @Test
    void "extract text response"() {
        HttpTestDataServer.IBM_PRICES.reset()
        // full-text-response
        String responseAsText = http.resource("/prices/IBM").fullTextResponse()
        responseAsText.should == "{\"price\": 100}"
        // full-text-response
    }

    @Test
    void "header"() {
        // pass-header
        def responseValue = http.resource("/full-echo", http.header("x-prop", "x-value")).get("x-prop")
        // pass-header
        responseValue.should(equal("x-value"))
    }
}
