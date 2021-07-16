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
import org.testingisdocumenting.webtau.server.route.WebtauRouter;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.http.Http.*;

public class WebtauFakeRestServerHandledRequestsTest {
    @Test
    public void shouldWaitOnACall() throws InterruptedException {
        WebtauRouter router = new WebtauRouter("customers");
        router.getJson("/customer/{id}", (params) -> aMapOf("getId", params.get("id")));
        router.postJson("/customer/{id}", (params) -> aMapOf("postId", params.get("id")));
        router.putJson("/customer/{id}", (params) -> aMapOf("putId", params.get("id")));

        try (WebtauFakeRestServer restServer = new WebtauFakeRestServer("router-crud-journal", 0)) {
            restServer.addOverride(router);
            restServer.start();

            Thread thread = new Thread(() -> {
                http.get(restServer.getBaseUrl() + "/customer/id3");
            });

            thread.start();

            restServer.getJournal().handledRequests.GET.waitTo(contain("/customer/id3"));

            thread.join();

            restServer.getJournal().handledRequests.GET.should(contain("/customer/id3"));
        }
    }
}
