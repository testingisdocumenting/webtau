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

package com.twosigma.webtau.expectation;

public class ActualPath {
    private String path;

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

    private String attachToPath(String suffix) {
        return isEmpty() ? suffix : path + suffix;
    }

    public boolean isEmpty() {
        return path.isEmpty();
    }

    @Override
    public String toString() {
        return path;
    }
}
