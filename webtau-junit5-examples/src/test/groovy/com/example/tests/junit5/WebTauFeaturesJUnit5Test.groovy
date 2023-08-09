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

package com.example.tests.junit5

import com.example.tests.junit5.config.HttpPersonaAuthHeaderProvider
import com.example.tests.junitlike.cfg.DynamicPortBaseUrlConfig
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.testingisdocumenting.webtau.browser.Browser
import org.testingisdocumenting.webtau.featuretesting.WebTauBrowserFeaturesTestData
import org.testingisdocumenting.webtau.featuretesting.WebTauRestFeaturesTestData
import org.testingisdocumenting.webtau.http.config.WebTauHttpConfigurations
import org.testingisdocumenting.webtau.http.testserver.FixedResponsesHandler
import org.testingisdocumenting.webtau.http.testserver.TestServer

class WebTauFeaturesJUnit5Test {
    private static final FixedResponsesHandler responseHandler = new FixedResponsesHandler()

    private static final TestServer testServer = new TestServer(responseHandler)

    private static final JUnit5FeatureTestRunner testRunner = new JUnit5FeatureTestRunner()

    @BeforeAll
    static void startServer() {
        testServer.startRandomPort()
        WebTauRestFeaturesTestData.registerEndPoints(testServer, responseHandler)
        WebTauBrowserFeaturesTestData.registerEndPoints(responseHandler)
    }

    @AfterAll
    static void stopServer() {
        testServer.stop()
    }

    @Test
    void weatherGroovyTest() {
        testRunner.runAndValidate(WeatherGroovyTest, testServer.uri.toString())
    }

    @Test
    void weatherJavaTest() {
        testRunner.runAndValidate(WeatherJavaTest, testServer.uri.toString())
    }

    @Test
    void implicitJavaHeader() {
        testRunner.runAndValidate(HttpImplicitJavaTest, testServer.uri.toString())
    }

    @Test
    void websocketSpringBoot() {
        testRunner.runAndValidate(WebSocketSpringBootTest, DynamicPortBaseUrlConfig.baseUrl())
    }

    @Test
    void searchByQuery() {
        testRunner.runAndValidate(WebSearchJavaTest, testServer.uri.toString())
    }

    @Test
    void browserCookies() {
        testRunner.runAndValidate(BrowserCookieJavaTest, testServer.uri.toString())
    }

    @Test
    void browserTables() {
        testRunner.runAndValidate(BrowserTablesJavaTest, testServer.uri.toString())
    }

    @Test
    void browserTestContainer() {
        testRunner.runAndValidate(BrowserTestContainerJavaTest, "http://host.testcontainers.internal:" + testServer.uri.port)
        Browser.browser.setDriver(null) // clean test container manually set driver
    }

    @Test
    void browserImplicitTestContainer() {
        runWithConfigFile("webtau.browser.testcontainers.properties", () -> {
            def baseUrl = "http://host.testcontainers.internal:" + testServer.uri.port
            testRunner.runAndValidate(BrowserImplicitTestContainerJavaTest, baseUrl, baseUrl)
        })
    }

    @Test
    void browserLocalFirefoxDriver() {
        runWithConfigFile("webtau.browser.firefox.properties", () -> {
            def baseUrl = testServer.uri.toString()
            testRunner.runAndValidate(BrowserLocalDriverJavaTest, baseUrl, baseUrl)
        })
    }

    @Test
    void browserLocalStorage() {
        testRunner.runAndValidate(BrowserLocalStorageJavaTest, testServer.uri.toString())
    }

    @Test
    void personaHttpTest() {
        def authHeaderProvider = new HttpPersonaAuthHeaderProvider()
        WebTauHttpConfigurations.add(authHeaderProvider)
        testRunner.runAndValidate(PersonaHttpJavaTest, testServer.uri.toString())
        WebTauHttpConfigurations.remove(authHeaderProvider)
    }

    @Test
    void dataTableDynamicTestsGroovy() {
        testRunner.runAndValidate(DynamicTestsGroovyTest, "")
    }

    @Test
    void dataTableDynamicTestsJava() {
        testRunner.runAndValidate(DynamicTestsJavaTest, "")
    }

    @Test
    void httpDataCoverage() {
        runWithConfigFile("webtau.routes.properties", () -> {
            testRunner.runAndValidate(NewYorkWeatherJavaTest, testServer.uri.toString())
        })
    }

    @Test
    void crudSeparatedJava() {
        testRunner.runAndValidate(CustomerCrudSeparatedJavaTest, "")
    }

    @Test
    void deferredBlock() {
        testRunner.runAndValidate(DeferredBlockJavaTest, "")
    }

    @Test
    void crudSeparatedGroovy() {
        testRunner.runAndValidate(CustomerCrudSeparatedGroovyTest, "")
    }

    @Test
    void customerHttpQueryJava() {
        testRunner.runAndValidate(CustomerQueryJavaTest, "")
    }

    @Test
    void customerHttpQueryGroovy() {
        testRunner.runAndValidate(CustomerQueryGroovyTest, "")
    }

    @Test
    void crudSeparatedFactoryJava() {
        testRunner.runAndValidate(CustomerCrudSeparatedTestFactoryIT, "")
    }

    @Test
    void docCapture() {
        testRunner.runAndValidate(CustomerDocCaptureTest, "")
    }

    @Test
    void stepTrace() {
        testRunner.runAndValidate(StepTraceJavaTest, "")
    }

    @Test
    void warning() {
        testRunner.runAndValidate(WarningJavaTest, "")
    }

    @Test
    void staticServerOverride() {
        testRunner.runAndValidate(StaticServerJavaTest, "")
    }

    @Test
    void fakeServerOverride() {
        testRunner.runAndValidate(FakeServerJavaTest, "")
    }

    @Test
    void routerProperties() {
        testRunner.runAndValidate(RouterPropertiesJavaTest, "")
    }

    private static void runWithConfigFile(String configFile, Runnable code) {
        System.setProperty("webtau.properties", configFile)
        try {
            code.run()
        } finally {
            System.setProperty("webtau.properties", "webtau.properties")
        }
    }
}