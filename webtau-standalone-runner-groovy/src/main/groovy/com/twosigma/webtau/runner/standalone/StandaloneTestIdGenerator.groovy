package com.twosigma.webtau.runner.standalone

import java.nio.file.Path

class StandaloneTestIdGenerator {
    private Map<String, Integer> entryNumberByName = [:].withDefault { 0 }

    synchronized String generate(Path testPath) {
        def entryName = testPath.fileName.toString()
        entryNumberByName[entryName]++

        return entryName + "-" + entryNumberByName[entryName]
    }
}
