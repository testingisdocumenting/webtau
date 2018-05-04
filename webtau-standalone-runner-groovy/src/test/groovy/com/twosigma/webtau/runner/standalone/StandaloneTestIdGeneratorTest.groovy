package com.twosigma.webtau.runner.standalone

import org.junit.Test

import java.nio.file.Paths

class StandaloneTestIdGeneratorTest {
    @Test
    void "generates ids using file name and the number of times file name was used"() {
        def generator = new StandaloneTestIdGenerator()
        assert generator.generate(Paths.get("path/filename.groovy")) == 'filename.groovy-1'
        assert generator.generate(Paths.get("path/filename.groovy")) == 'filename.groovy-2'
        assert generator.generate(Paths.get("another-path/filename.groovy")) == 'filename.groovy-3'
        assert generator.generate(Paths.get("another-path/another-file.groovy")) == 'another-file.groovy-1'
    }
}
