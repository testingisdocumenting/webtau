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

package org.testingisdocumenting.webtau.browser.driver;

import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Set;

public class WebDriverCreatorListeners {
    private static final List<WebDriverCreatorListener> listeners =
            ServiceLoaderUtils.load(WebDriverCreatorListener.class);

    private WebDriverCreatorListeners() {
    }

    public static void add(WebDriverCreatorListener listener) {
        listeners.add(listener);
    }

    public static void remove(WebDriverCreatorListener listener) {
        listeners.remove(listener);
    }

    public static void beforeDriverCreation() {
        listeners.forEach(WebDriverCreatorListener::beforeDriverCreation);
    }

    public static void afterDriverCreation(WebDriver webDriver) {
        listeners.forEach(l -> l.afterDriverCreation(webDriver));
    }

    public static void beforeDriverQuit(WebDriver webDriver) {
        listeners.forEach(l -> l.beforeDriverQuit(webDriver));
    }

    public static void afterDriverQuit(WebDriver webDriver) {
        listeners.forEach(l -> l.afterDriverQuit(webDriver));
    }
}
