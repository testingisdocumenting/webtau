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

package com.twosigma.webtau

import com.twosigma.webtau.console.ConsoleOutputs
import com.twosigma.webtau.console.ansi.Color
import com.twosigma.webtau.console.ansi.FontStyle
import com.twosigma.webtau.data.LazyTestResource
import com.twosigma.webtau.runner.standalone.StandaloneTestRunner
import com.twosigma.webtau.runner.standalone.TestsRunTerminateException
import com.twosigma.webtau.utils.RegexpUtils

import java.util.function.Supplier
import java.util.regex.Pattern

import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.none
import static com.twosigma.webtau.reporter.TestStep.createAndExecuteStep
import static com.twosigma.webtau.reporter.TokenizedMessage.tokenizedMessage

class WebTauGroovyDsl extends WebTauDsl {
    private static final Pattern PLACEHOLDER_PATTERN = ~/<(\w+)>/

    private static StandaloneTestRunner testRunner

    static void initWithTestRunner(StandaloneTestRunner testRunner) {
        this.testRunner = testRunner
    }

    static void scenario(String description, Closure code) {
        // if test runner is not defined it means that groovy script was run as a simple script
        // and not through webtau command line
        // in this case we just run scenario code eagerly instead of registering it
        if (!testRunner) {
            runAdHoc(description, code)
        } else {
            testRunner.scenario(description, code)
        }
    }

    static void sscenario(String description, Closure code) {
        if (!testRunner) {
            runAdHoc(description, code)
        } else {
            testRunner.sscenario(description, code)
        }
    }

    static void terminateAll(String reason) {
        throw new TestsRunTerminateException(reason)
    }

    /**
     * Multiple scenarios may need the same data setup. If you want those scenarios to run independently,
     * data needs to be initialized on the first request. Typically this is done by moving
     * initialization to `beforeAll` sort of function. In webtau you create lazy resources instead.
     *
     * <pre>
     * def lazySharedData = createLazyResource("resource name") {
     *      def result = callToInitializeTheResouce()
     *      return new MySharedData(firstName: result.firstName, score: result.score)
     * }
     * ...
     * scenario('scenario description') {
     *     http.get("/resource/${lazySharedData.firstName}") {
     *         ...
     *     }
     * }
     * </pre>
     * @param name name of the resource
     * @param supplier resource initialization function
     */
    static <E> E createLazyResource(String name, Supplier<E> supplier) {
        return new LazyTestResource<E>(name, supplier)
    }

    static Closure action(String description, Closure code) {
        return { args ->
            String withReplacedValues = replacePlaceholders(description, args)

            createAndExecuteStep(null, tokenizedMessage(none(withReplacedValues)),
                    { -> tokenizedMessage(none("done " + withReplacedValues)) },
                    { -> code.curry(args).call() })
        }
    }

    private static void runAdHoc(String description, Closure code) {
        ConsoleOutputs.out(Color.CYAN, "ad-hoc run ", Color.GREEN, FontStyle.BOLD, description)
        code.run()
    }

    private static String replacePlaceholders(String description, args) {
        return RegexpUtils.replaceAll(description, PLACEHOLDER_PATTERN, { matcher ->
            return args[matcher.group(1)]
        })
    }
}
