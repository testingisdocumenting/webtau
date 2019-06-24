package com.twosigma.webtau

import java.nio.file.Path

class TestFile {
    private final Path path
    private final String shortContainerId

    TestFile(Path path) {
        this(path, null)
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
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        TestFile testFile = (TestFile) o

        if (path != testFile.path) return false
        if (shortContainerId != testFile.shortContainerId) return false

        return true
    }

    int hashCode() {
        int result
        result = (path != null ? path.hashCode() : 0)
        result = 31 * result + (shortContainerId != null ? shortContainerId.hashCode() : 0)
        return result
    }


    @Override
    public String toString() {
        return "TestFile{" +
            "path=" + path +
            ", shortContainerId='" + shortContainerId + '\'' +
            '}';
    }
}
