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

package org.testingisdocumenting.webtau.data.time.listener

import org.testingisdocumenting.webtau.data.time.TestTime
import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils

class TimeRangeContextListeners {
    // TODO multithread support
    private static List<TimeRangeContextListener> listeners = ServiceLoaderUtils.load(TimeRangeContextListener)

    static void clear() {
        listeners.clear()
    }

    static void add(TimeRangeContextListener listener) {
        listeners.add(listener)
    }

    static void beforeCall(TestTime begin, TestTime end) {
        listeners.each { it.beforeCall(begin, end) }
    }

    static void afterCall(TestTime begin, TestTime end) {
        listeners.each { it.afterCall(begin, end) }
    }
}
