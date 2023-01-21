/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.server;

import org.junit.Test;
import org.testingisdocumenting.webtau.server.registry.WebTauServerHandledRequest;
import org.testingisdocumenting.webtau.server.route.WebTauRouter;

import static org.testingisdocumenting.webtau.Matchers.*;
import static org.testingisdocumenting.webtau.WebTauCore.actual;
import static org.testingisdocumenting.webtau.WebTauCore.greaterThanOrEqual;
import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.http.Http.*;
import static org.testingisdocumenting.webtau.server.WebTauServerFacade.*;

public class WebTauProxyServerTest {
    @Test
    public void shouldCaptureRequestResponseSuccessful()  {
        WebTauRouter router = new WebTauRouter("customers");
        router.put("/customer/{id}", (request) -> server.response(map("putId", request.param("id"))));

        try (WebTauServer restServer = server.fake("router-crud-for-proxy", router)) {
            try (WebTauServer proxyServer = server.proxy("proxy-for-journal", restServer.getBaseUrl())) {
                http.put(proxyServer.getBaseUrl() + "/customer/id3", map("hello", "world"), (header, body) -> {
                    body.get("putId").should(equal("id3"));
                });

                WebTauServerHandledRequest handledRequest = proxyServer.getJournal().getLastHandledRequest();
                actual(handledRequest.getUrl()).should(equal("/customer/id3"));
                actual(handledRequest.getMethod()).should(equal("PUT"));
                actual(handledRequest.getStatusCode()).should(equal(200));

                actual(handledRequest.getRequestType()).should(equal("application/json"));
                actual(handledRequest.getCapturedRequest()).should(equal("{\"hello\":\"world\"}"));

                actual(handledRequest.getResponseType()).should(equal("application/json"));
                actual(handledRequest.getCapturedResponse()).should(equal("{\"putId\":\"id3\"}"));

                actual(handledRequest.getStartTime()).shouldBe(greaterThanOrEqual(0));
                actual(handledRequest.getElapsedTime()).shouldBe(greaterThanOrEqual(0));
            }
        }
    }

    @Test
    public void shouldCaptureRequestResponseBrokenServer()  {
        WebTauRouter router = new WebTauRouter("customers-fail");
        router.put("/customer/{id}", (request) -> server.response(500, null));

        try (WebTauServer restServer = server.fake("router-crud-for-proxy-fail", router)) {
            try (WebTauServer proxyServer = server.proxy("proxy-for-journal-fail", restServer.getBaseUrl())) {
                http.put(proxyServer.getBaseUrl() + "/customer/id3", map("hello", "world"), (header, body) -> {
                    header.statusCode.should(equal(500));
                });

                WebTauServerHandledRequest handledRequest = proxyServer.getJournal().getLastHandledRequest();
                actual(handledRequest.getUrl()).should(equal("/customer/id3"));
                actual(handledRequest.getMethod()).should(equal("PUT"));
                actual(handledRequest.getStatusCode()).should(equal(500));

                actual(handledRequest.getRequestType()).should(equal("application/json"));
                actual(handledRequest.getCapturedRequest()).should(equal("{\"hello\":\"world\"}"));

                actual(handledRequest.getResponseType()).should(equal("text/plain"));
                actual(handledRequest.getCapturedResponse()).should(equal(""));

                actual(handledRequest.getStartTime()).shouldBe(greaterThanOrEqual(0));
                actual(handledRequest.getElapsedTime()).shouldBe(greaterThanOrEqual(0));
            }
        }
    }
}
