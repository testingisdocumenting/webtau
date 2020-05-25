/*
 *
 *  * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.testingisdocumenting.webtau.cli

import org.testingisdocumenting.webtau.cli.expectation.CliExitCode
import org.testingisdocumenting.webtau.cli.expectation.CliOutput
import org.testingisdocumenting.webtau.cli.expectation.CliValidationExitCodeOutputHandler

class CliExtension {
    static void run(Cli cli, String command, Closure handler) {
        cli.run(command, closureToCliValidationHandler(handler))
    }

    static void run(Cli cli, String command, ProcessEnv env, Closure handler) {
        cli.run(command, env, closureToCliValidationHandler(handler))
    }

    private static CliValidationExitCodeOutputHandler closureToCliValidationHandler(Closure validation) {
        return new CliValidationExitCodeOutputHandler() {
            @Override
            void handle(CliExitCode exitCode, CliOutput output, CliOutput error) {
                def cloned = validation.clone() as Closure
                cloned.delegate = new ValidatorDelegate(exitCode: exitCode, output: output, error: error)
                cloned.resolveStrategy = Closure.OWNER_FIRST
                cloned.call()
            }
        }
    }

    private static class ValidatorDelegate {
        CliExitCode exitCode
        CliOutput output
        CliOutput error
    }
}
