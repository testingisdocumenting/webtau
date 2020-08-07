/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.graphql;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.testingisdocumenting.webtau.http.HttpHeader;
import org.testingisdocumenting.webtau.http.config.HttpConfiguration;
import org.testingisdocumenting.webtau.http.config.HttpConfigurations;
import org.testingisdocumenting.webtau.utils.UrlUtils;

public class GraphQLTestBase implements HttpConfiguration {
    private static final GraphQLTestDataServer testServer = new GraphQLTestDataServer();

    @BeforeClass
    public static void startServer() {
        testServer.start();
    }

    @AfterClass
    public static void stopServer() {
        testServer.stop();
    }

    @Before
    public void initCfg() {
        HttpConfigurations.add(this);
    }

    @After
    public void cleanCfg() {
        HttpConfigurations.remove(this);
    }

    @Override
    public String fullUrl(String url) {
        if (UrlUtils.isFull(url)) {
            return url;
        }

        return UrlUtils.concat(testServer.getUri(), url);
    }

    @Override
    public HttpHeader fullHeader(String fullUrl, String passedUrl, HttpHeader given) {
        return given;
    }
}
