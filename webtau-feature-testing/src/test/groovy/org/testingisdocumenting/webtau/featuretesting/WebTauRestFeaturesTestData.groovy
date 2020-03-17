/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.featuretesting

import com.twosigma.webtau.http.testserver.TestServer
import com.twosigma.webtau.http.testserver.TestServerJsonResponse
import com.twosigma.webtau.http.testserver.TestServerRedirectResponse
import com.twosigma.webtau.http.testserver.TestServerResponse
import com.twosigma.webtau.utils.JsonUtils

class WebTauRestFeaturesTestData {
    static void registerEndPoints(TestServer testServer) {
        def temperature = [temperature: 88]
        testServer.registerGet("/weather", json(temperature))
        testServer.registerGet("/redirect", new TestServerRedirectResponse(HttpURLConnection.HTTP_MOVED_TEMP,
                testServer, "/weather"))
        testServer.registerGet("/city/London", json([time: "2018-11-27 13:05:00", weather: temperature]))
        testServer.registerPost("/employee", json([id: 'id-generated-2'], 201))
        testServer.registerGet("/employee/id-generated-2", json([firstName: 'FN', lastName: 'LN']))

    }

    private static TestServerResponse json(Map response, statusCode = 200) {
        return new TestServerJsonResponse(JsonUtils.serialize(response), statusCode)
    }
}
