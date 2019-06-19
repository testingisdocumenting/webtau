package com.twosigma.webtau

import java.nio.file.Path

class TestFile {
    private final Path path
    private final String shortFileName

    TestFile(Path path) {
        this(path, null)
    }

    TestFile(Path path, String shortFileName) {
        this.path = path
        this.shortFileName = shortFileName
    }

    Path getPath() {
        return path
    }

    String getShortFileName() {
        return shortFileName
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        TestFile testFile = (TestFile) o

        if (path != testFile.path) return false
        if (shortFileName != testFile.shortFileName) return false

        return true
    }

    int hashCode() {
        int result
        result = (path != null ? path.hashCode() : 0)
        result = 31 * result + (shortFileName != null ? shortFileName.hashCode() : 0)
        return result
    }


    @Override
    public String toString() {
        return "TestFile{" +
            "path=" + path +
            ", shortFileName='" + shortFileName + '\'' +
            '}';
    }
}
