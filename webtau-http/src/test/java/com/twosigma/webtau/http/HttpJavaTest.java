/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.http;

import com.twosigma.webtau.http.config.HttpConfiguration;
import com.twosigma.webtau.http.config.HttpConfigurations;
import com.twosigma.webtau.utils.UrlUtils;
import org.junit.*;

import static com.twosigma.webtau.Ddjt.equal;
import static com.twosigma.webtau.http.Http.http;

public class HttpJavaTest implements HttpConfiguration  {
    static HttpTestDataServer testServer = new HttpTestDataServer();

    private static final int PORT = 7824;

    @BeforeClass
    static void startServer() {
        testServer.start(PORT);
    }

    @AfterClass
    static void stopServer() {
        testServer.stop();
    }

    @Before
    void initCfg() {
        HttpConfigurations.add(this);
    }

    @After
    void cleanCfg() {
        HttpConfigurations.remove(this);
    }

    @Test
    void simpleObjectMappingExample() {
        http.get("/end-point-simple-object", (header, body) -> {
            body.get("k1").should(equal("v1"));
        });
    }

    @Test
    void simpleListMappingExample() {
        http.get("/end-point-simple-list", (header, body) -> {
            body.get(0).get("k1").should(equal("v1"));
        });
    }

    @Override
    public String fullUrl(String url) {
        if (UrlUtils.isFull(url)) {
            return url;
        }

        return UrlUtils.concat("http://localhost:${PORT}", url);
    }

    @Override
    public HttpHeader fullHeader(String fullUrl, String passedUrl, HttpHeader given) {
        return given;
    }
}
