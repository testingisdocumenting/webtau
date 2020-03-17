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

package org.testingisdocumenting.webtau.data.time

class TestTimeDSLUtils {
    private TestTimeDSLUtils() {
    }

    static HoursMinutesPart hoursMinutesFromMap(Map hoursMinutes) {
        if (hoursMinutes.size() != 1)
            throw new IllegalArgumentException("hours:minutes argument is expected to be a map with one element, key is hours and value is minutes")

        def hours = hoursMinutes.keySet().iterator().next()
        if (! (hours instanceof Integer))
            throw new IllegalArgumentException("hours in hours:minutes expected to be an Integer")

        def minutes = hoursMinutes.values().iterator().next()
        if (! (minutes instanceof Integer))
            throw new IllegalArgumentException("minutes in hours:minutes expected to be an Integer")

        return new HoursMinutesPart(hours: hours, minutes: minutes)
    }
}
