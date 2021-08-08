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

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

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

    @Test
    void "delegate environments"() {
        def dslDelegate = runClosureWithDelegate {
            email = 'hello'
            environments {
                dev {
                    email = 'dev-hello'
                }

                beta {
                    email = 'beta'
                }
            }
        }

        dslDelegate.envValuesToMap("dev").should == [email: "dev-hello"]
        dslDelegate.envValuesToMap("beta").should == [email: "beta"]
    }

    @Test
    void "environment partial value override"() {
        def dslDelegate = runClosureWithDelegate {
            perKey = [id1: 'value1', id2: 'value2']
            environments {
                dev {
                    perKey.id1 = 'value1-dev'
                    perKey.id3 = 'value3-dev'
                }

                beta {
                    perKey.id3 = 'value3-beta'
                }
            }
        }

        println(dslDelegate.envValuesToMap("dev"))
        println(dslDelegate.envValuesToMap("beta"))

        dslDelegate.envValuesToMap("dev").should == [perKey: [id1: 'value1-dev', id2: 'value2', id3: 'value3-dev']]
        dslDelegate.envValuesToMap("beta").should == [perKey: [id1: 'value1', id2: 'value2', id3: 'value3-beta']]
    }

    @Test
    void "delegate environments should forbid config values outside specific env"() {
        code {
            runClosureWithDelegate {
                email = 'hello'
                environments {
                    email = 'dev-hello'
                }
            }
        } should throwException("config values must be defined within a specific environment\n" +
                "usage for environments should look like this:\n" +
                "environments {\n" +
                "   dev {\n" +
                "   }\n" +
                "}\n")
    }

    @Test
    void "delegate environments should validate environments is passed closure"() {
        code {
            runClosureWithDelegate {
                email = 'hello'
                environments {
                    dev "hello"
                }
            }
        } should throwException("usage for environments should look like this:\n" +
                "environments {\n" +
                "   dev {\n" +
                "   }\n" +
                "}\n")
    }

    @Test
    void "delegate personas"() {
        def dslDelegate = runClosureWithDelegate {
            email = 'hello'
            personas {
                Alice {
                    email = 'alice-email'
                }

                Bob {
                    email = 'bob-email'
                }
            }
        }

        dslDelegate.personaValuesToMap('Alice').should == [email: 'alice-email']
        dslDelegate.personaValuesToMap('Bob').should == [email: 'bob-email']
    }

    @Test
    void "delegate personas complex object"() {
        def dslDelegate = runClosureWithDelegate {
            cliEnv = [
                    COMMON: 'common value',
                    ANOTHER_COMMON: 'another common value']

            personas {
                Alice {
                    cliEnv.CREDENTIALS = 'alice-token'
                    cliEnv.EXTRA_ALICE = 'extra alice'
                }

                Bob {
                    cliEnv.CREDENTIALS = 'bob-token'
                    cliEnv.EXTRA_BOB = 'extra bob'
                }
            }
        }

        println dslDelegate.personaValuesToMap('Alice')

        println dslDelegate
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