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

package org.testingisdocumenting.webtau.browser;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.Map;
import java.util.stream.Collectors;

public class Cookies {
    private final WebDriver driver;

    Cookies(WebDriver driver) {
        this.driver = driver;
    }

    public void add(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        driver.manage().addCookie(cookie);
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
        driver.manage().deleteCookieNamed(name);
    }

    public void deleteAll() {
        driver.manage().deleteAllCookies();
    }
}
