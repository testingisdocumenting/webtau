/*
 * Copyright 2020 webtau maintainers
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

package scenarios

import withlisteners.SampleTestListener

import java.nio.file.Files

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg
import static webtau.CliCommands.*

def repl = createLazyResource { webtauCli.runInBackground("repl --noColor --workingDir=${cfg.workingDir} " +
        "testscripts/browserSanity.groovy testscripts/scriptWithSyntaxError.groovy") }

scenario('simple groovy repl') {
    repl << "2 + 2\n"

    repl.output.waitTo contain("4")

    repl.output.should contain("4")
    repl.clearOutput()
    repl.output.shouldNot contain("4")

    repl << "cfg\n"
    repl.output.waitTo contain("url:")
}

scenario('http call') {
    repl.with {
        clearOutput()
        send('http.get("https://jsonplaceholder.typicode.com/todos/1")\n')
        output.waitTo contain('executed')
    }

    cli.doc.capture('http-repl-output')
    fs.textContent(cfg.docArtifactsPath.resolve('http-repl-output/out.txt')).should contain(
            'header.statusCode equals 200')
}

scenario('should list test files on start') {
    repl.clearOutput()
    repl.output.waitTo contain('browserSanity.groovy')

    cli.doc.capture('repl-tests-listing')
}

scenario('test listing') {
    repl.clearOutput()
    repl << "ls\n"
    repl.output.waitTo contain('browserSanity.groovy')

    cli.doc.capture('repl-tests-listing')
}

scenario('report syntax error during test file parse') {
    repl.clearOutput()
    repl << "s 'scriptWith'\n"

    repl.output.waitTo contain('groovy.lang.MissingPropertyException: No such property: aaa')
}

scenario('before all must be called only once and after all listener should not be called at all') {
    def localRepl = webtauCli.runInBackground("repl --noColor --workingDir=${cfg.workingDir} " +
            "--config=withlisteners/webtau.cfg.groovy " +
            "withlisteners/dummy.groovy  ")
    localRepl.with {
        send('s 0\n')
        send('r 0\n')
        send('r 1\n')
    }

    localRepl.output.waitTo contain('[.] dummy test two')

    def markerPath = cfg.workingDir.resolve(SampleTestListener.FILE_NAME)
    def testListenerContent = Files.readAllLines(markerPath).join('\n')
    testListenerContent.should == 'beforeFirstTest'

    Files.delete(markerPath)
}