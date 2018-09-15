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

package com.twosigma.webtau.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.html5.WebStorage;

import java.util.Set;

public class LocalStorage {
    private final org.openqa.selenium.html5.LocalStorage localStorage;

    LocalStorage(WebDriver driver) {
        WebStorage webStorage = (WebStorage) driver;
        this.localStorage = webStorage.getLocalStorage();
    }

    public String getItem(String key) {
        return localStorage.getItem(key);
    }

    public Set<String> keySet() {
        return localStorage.keySet();
    }

    public void setItem(String key, String value) {
        localStorage.setItem(key, value);
    }

    public String removeItem(String key) {
        return localStorage.removeItem(key);
    }

    public void clear() {
        localStorage.clear();
    }

    public int size() {
        return localStorage.size();
    }
}
