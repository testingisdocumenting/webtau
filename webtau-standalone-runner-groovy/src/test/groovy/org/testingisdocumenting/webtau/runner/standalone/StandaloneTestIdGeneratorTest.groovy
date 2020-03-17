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

package org.testingisdocumenting.webtau.runner.standalone

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
