/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.app.cfg

import org.testingisdocumenting.webtau.TestFile

import org.junit.Test
import org.testingisdocumenting.webtau.cfg.WebTauConfig

import java.nio.file.Path
import java.nio.file.Paths

class WebTauGroovyCliArgsConfigHandlerTest {
    @Test
    void "sets file config related values during first run and overrides other cfg values during second"() {
        def handler = new WebTauGroovyCliArgsConfigHandler('--env=dev', '--workingDir=/root/a',
            '--config=mycfg.groovy', '--url=http://localhost:3434', "test.groovy")

        def cfg = createConfig()

        handler.onAfterCreate(cfg)

        cfg.env.should == 'dev'
        cfg.workingDir.asList()*.toString().should == ['root', 'a']
        cfg.configFileNameValue.asString.should == 'mycfg.groovy'
        cfg.baseUrl.should == ""

        handler.onAfterCreate(cfg)

        cfg.baseUrl.should == 'http://localhost:3434'
    }

    @Test
    void "should recurse test files"() {
        def expectedFiles = [
            Paths.get("rootScenarios.groovy"),
            Paths.get("first-nested-dir", "nestedScenarios.groovy"),
            Paths.get("first-nested-dir", "second-nested-dir", "nestedNestedScenarios.groovy"),
            Paths.get("sibling-nested-dir", "siblingScenarios.groovy")
        ]
        runRecursiveTestDiscoveryTest('testScenarios', expectedFiles)
    }

    @Test
    void "recursive test discovery ignores non-groovy files"() {
        def expectedFiles = [Paths.get('foo.groovy')]
        runRecursiveTestDiscoveryTest('testScenarios2', expectedFiles)
    }

    private static runRecursiveTestDiscoveryTest(String scenariosDir, List<Path> expectedTestFilePaths) {
        def handler = new WebTauGroovyCliArgsConfigHandler(scenariosDir)

        def cfg = createConfig()

        handler.onAfterCreate(cfg)
        def testFiles = handler.testFilesWithFullPath().sort { it.path }

        def cwd = Paths.get("").toAbsolutePath()
        def scenariosRoot = cwd.resolve(scenariosDir)

        def expectedFilePaths = expectedTestFilePaths.collect {
            new TestFile(scenariosRoot.resolve(it), it.collect { it.toString() }.join('/'))
        }.sort {
            it.path
        }

        testFiles.should == expectedFilePaths
    }

    private static WebTauConfig createConfig() {
        return new WebTauConfig()
    }
}
