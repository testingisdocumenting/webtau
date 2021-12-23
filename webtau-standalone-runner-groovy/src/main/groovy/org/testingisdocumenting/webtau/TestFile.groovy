/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau

import java.nio.file.Path

class TestFile {
    private final Path path
    private final String shortContainerId

    TestFile(Path path) {
        this(path, path.fileName.toString())
    }

    TestFile(Path path, String shortContainerId) {
        this.path = path
        this.shortContainerId = shortContainerId
    }

    Path getPath() {
        return path
    }

    String getShortContainerId() {
        return shortContainerId
    }

    boolean equals(o) {
        if (this.is(o)) {
            return true
        }

        if (getClass() != o.class) {
            return false
        }

        TestFile testFile = (TestFile) o

        if (path != testFile.path) {
            return false
        }

        if (shortContainerId != testFile.shortContainerId) {
            return false
        }

        return true
    }

    int hashCode() {
        int result
        result = (path != null ? path.hashCode() : 0)
        result = 31 * result + (shortContainerId != null ? shortContainerId.hashCode() : 0)
        return result
    }


    @Override
    String toString() {
        return "TestFile{" +
            "path=" + path +
            ", shortContainerId='" + shortContainerId + '\'' +
            '}'
    }
}
