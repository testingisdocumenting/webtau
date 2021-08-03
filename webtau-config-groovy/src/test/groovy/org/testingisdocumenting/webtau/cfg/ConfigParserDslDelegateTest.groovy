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

package org.testingisdocumenting.webtau.cfg

import org.junit.Test

class ConfigParserDslDelegateTest {
    @Test
    void "delegate basic properties"() {
        def dslDelegate = runClosureWithDelegate {
            email = 'hello'
            cliPath = ['p1', 'p2']
            cliEnv.my_var.nested = 'webtau'
        }

        dslDelegate.toMap().should == [email: 'hello',
                                       cliEnv: [my_var: [nested: 'webtau']],
                                       cliPath: ['p1', 'p2']]
    }

    private static ConfigParserDslDelegate runClosureWithDelegate(Closure closure) {
        def dslDelegate = new ConfigParserDslDelegate()
        def cloned = closure.clone() as Closure
        cloned.delegate = dslDelegate
        cloned.resolveStrategy = Closure.DELEGATE_FIRST

        cloned.run()

        return dslDelegate
    }
}