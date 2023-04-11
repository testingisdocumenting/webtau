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

package org.testingisdocumenting.webtau.http;

import org.junit.Test;
import org.testingisdocumenting.webtau.http.resource.HttpLazyResponseValue;

import static org.testingisdocumenting.webtau.Matchers.*;
import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.http.Http.*;
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.*;

public class HttpResourceJavaTest extends HttpTestBase {
    private final HttpLazyResponseValue priceNoParams = http.resource("/end-point").get("price");
    private final HttpLazyResponseValue price = http.resource("/end-point/:id").get("price");
    private final HttpLazyResponseValue nestedProp = http.resource("/end-point").get("object.k3");
    private final HttpLazyResponseValue livePrice = http.resource("/prices/:ticker").get("price");

    private final HttpLazyResponseValue myObj = http.resource("/end-point/:param1/:param2").get("object");

    @Test
    public void resourceNodeNoRouteParam() {
        runAndValidateOutput("> expecting value of /end-point: price to equal 100\n" +
                "  > executing HTTP GET http://localhost:port/end-point\n" +
                "    . header.statusCode equals 200 (Xms)\n" +
                "    \n" +
                "    response (application/json):\n" +
                "    {\n" +
                "      \"id\": 10,\n" +
                "      \"price\": __100__,\n" +
                "      \"amount\": 30,\n" +
                "      \"list\": [1, 2, 3],\n" +
                "      \"object\": {\"k1\": \"v1\", \"k2\": \"v2\", \"k3\": \"v3\"},\n" +
                "      \"complexList\": [{\"id\": \"id1\", \"k1\": \"v1\", \"k2\": 30}, {\"id\": \"id2\", \"k1\": \"v11\", \"k2\": 40}]\n" +
                "    }\n" +
                "  . executed HTTP GET http://localhost:port/end-point (Xms)\n" +
                ". value of /end-point: price equals 100 (Xms)", () -> priceNoParams.should(equal(100)));
    }

    @Test
    public void nestedPathNoRouteParams() {
        nestedProp.should(equal("v3"));
    }

    @Test
    public void resourceNodeUseParam() {
        price.of("id1").should(equal(120));
        price.of("id2").should(equal(80));
    }

    @Test
    public void resourceNodeMultipleParam() {
        runAndValidateOutput("> expecting value of /end-point/10/20: object to equal {\"k1\": \"v1_\", \"k2\": \"v2_\", \"k3\": \"v3_\"}\n" +
                "  > executing HTTP GET http://localhost:port/end-point/10/20\n" +
                "    . header.statusCode equals 200 (Xms)\n" +
                "    \n" +
                "    response (application/json):\n" +
                "    {\n" +
                "      \"id\": 10,\n" +
                "      \"price\": 120,\n" +
                "      \"amount\": 30,\n" +
                "      \"list\": [1, 2, 3],\n" +
                "      \"object\": {\"k1\": __\"v1_\"__, \"k2\": __\"v2_\"__, \"k3\": __\"v3_\"__},\n" +
                "      \"complexList\": [{\"id\": \"id1\", \"k1\": \"v1\", \"k2\": 30}, {\"id\": \"id2\", \"k1\": \"v11\", \"k2\": 40}]\n" +
                "    }\n" +
                "  . executed HTTP GET http://localhost:port/end-point/10/20 (Xms)\n" +
                ". value of /end-point/10/20: object equals {\"k1\": \"v1_\", \"k2\": \"v2_\", \"k3\": \"v3_\"} (Xms)",
                () -> myObj.of("param1", "10", "param2", "20").should(
                        equal(map("k1", "v1_", "k2", "v2_", "k3", "v3_"))));
    }

    @Test
    public void waitForMultipleTimes() {
        HttpTestDataServer.IBM_PRICES.reset();
        runAndValidateOutput("> waiting for value of /prices/IBM: price to be greater than 115\n" +
                "  > [1/3] executing HTTP GET http://localhost:port/prices/IBM\n" +
                "    . header.statusCode equals 200 (Xms)\n" +
                "    \n" +
                "    response (application/json):\n" +
                "    {\n" +
                "      \"price\": **100**\n" +
                "    }\n" +
                "  . [1/3] executed HTTP GET http://localhost:port/prices/IBM (Xms)\n" +
                "  > [3/3] executing HTTP GET http://localhost:port/prices/IBM\n" +
                "    . header.statusCode equals 200 (Xms)\n" +
                "    \n" +
                "    response (application/json):\n" +
                "    {\n" +
                "      \"price\": ~~120~~\n" +
                "    }\n" +
                "  . [3/3] executed HTTP GET http://localhost:port/prices/IBM (Xms)\n" +
                        ". value of /prices/IBM: price greater than 115 (Xms)",
                () -> livePrice.of("IBM").waitToBe(greaterThan(115)));
    }

    @Test
    public void waitForValueOnce() {
        HttpTestDataServer.IBM_PRICES.reset();
        runAndValidateOutput("> waiting for value of /prices/IBM: price to equal 100\n" +
                "  > executing HTTP GET http://localhost:port/prices/IBM\n" +
                "    \n" +
                "    response (application/json):\n" +
                "    {\n" +
                "      \"price\": __100__\n" +
                "    }\n" +
                "  . executed HTTP GET http://localhost:port/prices/IBM (Xms)\n" +
                ". value of /prices/IBM: price equals 100 (Xms)",
                () -> livePrice.of("IBM").waitTo(equal(100)));
    }

    @Test
    public void waitForValueFailure() {
        HttpTestDataServer.IBM_PRICES.reset();
        runExpectExceptionAndValidateOutput(AssertionError.class, "> waiting for value of /prices/IBM: price to equal 1000\n" +
                "  > executing HTTP GET http://localhost:port/prices/IBM\n" +
                "    \n" +
                "    response (application/json):\n" +
                "    {\n" +
                "      \"price\": **100**\n" +
                "    }\n" +
                "  . executed HTTP GET http://localhost:port/prices/IBM (Xms)\n" +
                "X failed waiting for value of /prices/IBM: price to equal 1000:\n" +
                "      actual: 100 <java.lang.Integer>\n" +
                "    expected: 1000 <java.lang.Integer> (Xms)",
                () -> livePrice.of("IBM").waitTo(equal(1000), 10, 5));
    }

    @Test
    public void noParamsDefinedWhenOneProvided() {
        code(() -> priceNoParams.of("test").should(equal(120)))
                .should(throwException("route definition must have one parameter, but definition has zero: /end-point"));
    }

    @Test
    public void twoParamsDefinitionOneProvided() {
        code(() -> myObj.of("test").should(equal(120)))
                .should(throwException("route parameter names mismatch with provided, expected names: [param1, param2], given: [param1]"));
    }

    @Test
    public void twoParamsDefinitionTwoWrongProvided() {
        code(() -> myObj.of("param1", "test", "param3", "tost").should(equal(120)))
                .should(throwException("route parameter names mismatch with provided, expected names: [param1, param2], given: [param1, param3]"));
    }
}
