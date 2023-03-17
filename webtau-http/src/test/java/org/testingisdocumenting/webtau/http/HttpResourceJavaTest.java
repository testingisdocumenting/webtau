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
import org.testingisdocumenting.webtau.http.resource.HttpLazyDataNode;

import static org.testingisdocumenting.webtau.Matchers.*;
import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.http.Http.*;

public class HttpResourceJavaTest extends HttpTestBase {
    private final HttpLazyDataNode priceNoParams = http.resource("/end-point").body.get("price");
    private final HttpLazyDataNode price = http.resource("/end-point/:id").body.get("price");
    private final HttpLazyDataNode livePrice = http.resource("/prices/:ticker").body.get("price");

    private final HttpLazyDataNode myObj = http.resource("/end-point/:param1/:param2").body.get("object");

    @Test
    public void resourceNodeNoRouteParam() {
        priceNoParams.should(equal(100));
    }

    @Test
    public void resourceNodeUseParam() {
        price.of("id1").should(equal(120));
        price.of("id2").should(equal(80));
    }

    @Test
    public void resourceNodeMultipleParam() {
        myObj.of("param1", "10", "param2", "20").should(
                equal(map("k1", "v1_", "k2", "v2_", "k3", "v3_")));
    }

    @Test
    public void waitForValue() {
        HttpTestDataServer.IBM_PRICES.reset();
        livePrice.of("IBM").waitToBe(greaterThan(115));
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
