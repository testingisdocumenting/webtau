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

package com.twosigma.webtau.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.html5.WebStorage;

import java.util.Set;

public class LocalStorage {
    private final WebDriver driver;

    LocalStorage(WebDriver driver) {
        this.driver = driver;
    }

    public String getItem(String key) {
        return getLocalStorage().getItem(key);
    }

    public Set<String> keySet() {
        return getLocalStorage().keySet();
    }

    public void setItem(String key, String value) {
        getLocalStorage().setItem(key, value);
    }

    public String removeItem(String key) {
        return getLocalStorage().removeItem(key);
    }

    public void clear() {
        getLocalStorage().clear();
    }

    public int size() {
        return getLocalStorage().size();
    }

    private org.openqa.selenium.html5.LocalStorage getLocalStorage() {
        WebStorage webStorage = (WebStorage) driver;
        return webStorage.getLocalStorage();
    }
}
