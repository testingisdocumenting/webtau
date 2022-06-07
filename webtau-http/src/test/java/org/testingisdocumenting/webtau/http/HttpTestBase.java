/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.http;

import org.testingisdocumenting.webtau.documentation.DocumentationArtifactsLocation;
import org.testingisdocumenting.webtau.http.config.WebTauHttpConfiguration;
import org.testingisdocumenting.webtau.http.config.WebTauHttpConfigurations;
import org.testingisdocumenting.webtau.utils.UrlUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.nio.file.Path;
import java.nio.file.Paths;

public class HttpTestBase implements WebTauHttpConfiguration {
    protected static final HttpTestDataServer testServer = new HttpTestDataServer();
    private static Path existingDocRoot;

    @BeforeClass
    public static void startServer() {
        testServer.start();
    }

    @AfterClass
    public static void stopServer() {
        testServer.stop();
    }

    @Before
    public void setupDocArtifacts() {
        existingDocRoot = DocumentationArtifactsLocation.getRoot();
        DocumentationArtifactsLocation.setRoot(Paths.get("doc-artifacts"));
    }

    @After
    public void restoreDocArtifacts() {
        DocumentationArtifactsLocation.setRoot(existingDocRoot);
    }

    @Before
    public void initCfg() {
        WebTauHttpConfigurations.add(this);
    }

    @After
    public void cleanCfg() {
        WebTauHttpConfigurations.remove(this);
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
