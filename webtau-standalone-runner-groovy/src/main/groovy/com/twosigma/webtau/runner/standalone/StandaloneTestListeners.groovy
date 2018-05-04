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

package com.twosigma.webtau.runner.standalone

import com.twosigma.webtau.utils.ServiceLoaderUtils

import java.nio.file.Path

class StandaloneTestListeners {
    private static Set<StandaloneTestListener> listeners = ServiceLoaderUtils.load(StandaloneTestListener)

    static void add(StandaloneTestListener listener) {
        listeners.add(listener)
    }

    static void remove(StandaloneTestListener listener) {
        listeners.remove(listener)
    }

    static void beforeFirstTest() {
        listeners.each { it.beforeFirstTest() }
    }

    static void beforeScriptParse(Path scriptPath) {
        listeners.each { it.beforeScriptParse(scriptPath) }
    }

    static void beforeTestRun(StandaloneTest test) {
        listeners.each { it.beforeTestRun(test) }
    }

    static void afterTestRun(StandaloneTest test) {
        listeners.each { it.afterTestRun(test) }
    }

    static void afterAllTests() {
        listeners.each { it.afterAllTests() }
    }
}
