/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation;

import java.util.Objects;

public class ActualPath {
    public static final ActualPath UNDEFINED = new ActualPath("");

    private final String path;

    public ActualPath(String path) {
        if (path == null) {
            throw new IllegalArgumentException("path cannot be null");
        }

        this.path = path;
    }

    public ActualPath property(String propName) {
        return new ActualPath(isEmpty() ? propName : path + "." + propName);
    }

    public ActualPath index(int idx) {
        return new ActualPath(path + "[" + idx + "]");
    }

    public String getPath() {
        return path;
    }

    public boolean isEmpty() {
        return path.isEmpty();
    }

    @Override
    public String toString() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActualPath that = (ActualPath) o;
        return path.equals(that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
