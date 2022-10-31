/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.http.datacoverage;

import java.util.Set;
import java.util.TreeSet;

public class HttpOperationDataCoverage {
    private final Set<String> observedPaths;
    private final Set<String> touchedPaths;

    public HttpOperationDataCoverage() {
        this.observedPaths = new TreeSet<>();
        this.touchedPaths = new TreeSet<>();
    }

    public void addObservedPaths(Set<String> paths) {
        observedPaths.addAll(paths);
    }

    public void addTouchedPaths(Set<String> paths) {
        touchedPaths.addAll(paths);
    }

    public int countNumberOfTouchedPaths() {
        return touchedPaths.size();
    }

    public int countTotalNumberOfPaths() {
        return observedPaths.size();
    }

    public int countNumberOfUntouchedPaths() {
        return observedPaths.size() - touchedPaths.size();
    }

    public Set<String> computeUntouchedPaths() {
        TreeSet<String> result = new TreeSet<>(observedPaths);
        result.removeAll(touchedPaths);

        return result;
    }
}
