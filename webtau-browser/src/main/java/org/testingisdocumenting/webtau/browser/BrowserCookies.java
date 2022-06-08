/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.browser;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.testingisdocumenting.webtau.reporter.WebTauStep;

import java.util.Map;
import java.util.stream.Collectors;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class BrowserCookies {
    private final WebDriver driver;

    BrowserCookies(WebDriver driver) {
        this.driver = driver;
    }

    public void add(String name, String value) {
        WebTauStep.createAndExecuteStep(
                tokenizedMessage(action("setting browser cookie"), id(name), TO, stringValue(value)),
                () -> tokenizedMessage(action("set browser cookie"), id(name), TO, stringValue(value)),
                () -> {
                    Cookie cookie = new Cookie(name, value);
                    driver.manage().addCookie(cookie);
                });
    }

    public String get(String name) {
        return driver.manage().getCookieNamed(name).getValue();
    }

    public Map<String, String> getAll() {
        return driver.manage().getCookies().stream().collect(Collectors.toMap(
                Cookie::getName,
                Cookie::getValue));
    }

    public void delete(String name) {
        WebTauStep.createAndExecuteStep(
                tokenizedMessage(action("deleting browser cookie"), id(name)),
                () -> tokenizedMessage(action("deleted browser cookie"), id(name)),
                () -> driver.manage().deleteCookieNamed(name));
    }

    public void deleteAll() {
        WebTauStep.createAndExecuteStep(
                tokenizedMessage(action("deleting all browser cookies")),
                () -> tokenizedMessage(action("deleted all browser cookies")),
                () -> driver.manage().deleteAllCookies());
    }
}
