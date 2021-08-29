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
            // basic-properties
            email = 'hello'
            cliPath = ['p1', 'p2']
            // basic-properties
        }

        dslDelegate.toMap().should == [email: 'hello',
                                       cliPath: ['p1', 'p2']]
    }

    @Test
    void "use basic properties in expressions"() {
        def dslDelegate = runClosureWithDelegate {
            // basic-properties
            userId = 'user-a'
            url = "https://${userId}.host"
            // basic-properties
        }

        dslDelegate.toMap().should == [userId: 'user-a',
                                       url: 'https://user-a.host']
    }

    @Test
    void "nested properties"() {
        def dslDelegate = runClosureWithDelegate {
            // complex-properties
            complex.my_var.nested = 'webtau' // direct nested assignment

            complex { // scope definition for multiple assignment
                abc = 'abc_value'
                EDF = 'edf_value'
                subNested {
                    nested = 'nested_nested'
                }
            }

            anotherComplex = [nested: 'vn'] // map based assignment
            anotherComplex.anotherNested = 'an'
            // complex-properties

            email = 'hello'
        }

        dslDelegate.toMap().should == [email: 'hello',
                                       complex: [my_var: [nested: 'webtau'], abc: 'abc_value', EDF: 'edf_value',
                                                 subNested: [nested: 'nested_nested']],
                                       anotherComplex: [nested: 'vn', anotherNested: 'an']]
    }

    @Test
    void "delegate environments"() {
        def dslDelegate = runClosureWithDelegate {
            // environment-override
            email = 'hello'
            server = 'my-server'
            environments {
                dev {
                    email = 'dev-hello'
                }

                beta {
                    email = 'beta'
                }

                prod {
                    server = 'prod-server'
                }
            }
            // environment-override
        }

        dslDelegate.toMap().should == [email: "hello", server: "my-server"]
        dslDelegate.combinedValuesForEnv("dev").should == [email: "dev-hello", server: "my-server"]
        dslDelegate.combinedValuesForEnv("beta").should == [email: "beta", server: "my-server"]
        dslDelegate.combinedValuesForEnv("prod").should == [email: "hello", server: "prod-server"]
    }

    @Test
    void "delegate environments list objects"() {
        def dslDelegate = runClosureWithDelegate {
            // environment-override
            email = 'hello'
            list = [1, 2, 3, 4]
            environments {
                dev {
                    list = []
                }

                beta {
                    list = [2, 4]
                }

                prod {
                }
            }
            // environment-override
        }

        dslDelegate.toMap().should == [email: "hello", list: [1, 2, 3, 4]]
        dslDelegate.combinedValuesForEnv("dev").should == [email: "hello", list: []]
        dslDelegate.combinedValuesForEnv("beta").should == [email: "hello", list: [2, 4]]
        dslDelegate.combinedValuesForEnv("prod").should == [email: "hello", list: [1, 2, 3, 4]]
    }

    @Test
    void "delegate environments and map object"() {
        def dslDelegate = runClosureWithDelegate {
            email = 'hello'
            server = 'my-server'

            myComplex {
                nested = 'nested_value'
            }

            environments {
                dev {
                    email = 'dev-hello'
                }

                beta {
                    email = 'beta'
                }

                prod {
                    server = 'prod-server'
                }
            }
        }

        dslDelegate.toMap().should == [email: "hello", server: "my-server", myComplex: [nested: "nested_value"]]
        dslDelegate.combinedValuesForEnv("dev").should == [email: "dev-hello", server: "my-server",
                                                           myComplex: [nested: "nested_value"]]
        dslDelegate.combinedValuesForEnv("beta").should == [email: "beta", server: "my-server",
                                                            myComplex: [nested: "nested_value"]]
        dslDelegate.combinedValuesForEnv("prod").should == [email: "hello", server: "prod-server",
                                                            myComplex: [nested: "nested_value"]]
    }

    @Test
    void "environment partial value override"() {
        def dslDelegate = runClosureWithDelegate {
            // environment-complex-override
            complex = [id1: 'value1', id2: 'value2']
            environments {
                dev {
                    complex.id1 = 'value1-dev'
                    complex.id3 = 'value3-dev'
                }

                beta {
                    complex.id3 = 'value3-beta'
                }
            }
            // environment-complex-override
        }

        dslDelegate.envValuesToMap("dev").should == [complex: [id1: 'value1-dev', id2: 'value2', id3: 'value3-dev']]
        dslDelegate.envValuesToMap("beta").should == [complex: [id1: 'value1', id2: 'value2', id3: 'value3-beta']]
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
            // persona-overrides
            email = 'hello'
            personas {
                Alice {
                    email = 'alice-email'
                }

                Bob {
                    email = 'bob-email'
                }
            }
            // persona-overrides
        }

        dslDelegate.personaValuesToMap('Alice').should == [email: 'alice-email']
        dslDelegate.personaValuesToMap('Bob').should == [email: 'bob-email']

        code {
            dslDelegate.personaValuesToMap('Unknown').should == [:]
        } should throwException("No person found: Unknown")
    }

    @Test
    void "delegate personas complex object"() {
        def dslDelegate = runClosureWithDelegate {
            // persona-complex-overrides
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
            // persona-complex-overrides
        }

        dslDelegate.personaValuesToMap('Alice').should == [cliEnv: [COMMON: 'common value',
                                                           ANOTHER_COMMON: 'another common value',
                                                           CREDENTIALS: 'alice-token',
                                                           EXTRA_ALICE: 'extra alice']]

        dslDelegate.personaValuesToMap('Bob').should == [cliEnv: [COMMON: 'common value',
                                                           ANOTHER_COMMON: 'another common value',
                                                           CREDENTIALS: 'bob-token',
                                                           EXTRA_BOB: 'extra bob']]
    }

    @Test
    void "delegate personas complex object inside environment"() {
        def dslDelegate = runClosureWithDelegate {
            // complex-environment-persona
            cliEnv = [
                    COMMON: 'common value',
                    ANOTHER_COMMON: 'another common value']

            personas {
                Alice {
                    cliEnv.CREDENTIALS = 'alice-token' // default Alice's specific values
                    cliEnv.EXTRA_ALICE = 'extra alice'
                }

                Bob {
                    cliEnv.CREDENTIALS = 'bob-token'
                    cliEnv.EXTRA_BOB = 'extra bob'
                }
            }

            environments {
                dev {
                    personas {
                        Alice {
                            cliEnv.CREDENTIALS = 'alice-dev-token' // Alice's overrides for dev environment
                            cliEnv.EXTRA_ALICE = 'extra dev alice'
                            cliEnv.EXTRA_DEV_V = 'extra dev alice v'
                        }

                        Bob {
                            cliEnv.CREDENTIALS = 'bob-dev-token'
                            cliEnv.EXTRA_BOB = 'extra dev bob'
                        }
                    }
                }

                beta {
                    personas {
                        Alice {
                            cliEnv.CREDENTIALS = 'alice-beta-token'
                            cliEnv.EXTRA_ALICE = 'extra beta alice'
                            cliEnv.EXTRA_BETA_V = 'extra beta alice v'
                        }

                        Bob {
                            cliEnv.CREDENTIALS = 'bob-beta-token'
                            cliEnv.EXTRA_BOB = 'extra beta bob'
                        }
                    }
                }
            }
            // complex-environment-persona
        }

        dslDelegate.envPersonaValuesToMap('dev', 'Alice').should == [cliEnv: [
                COMMON: 'common value', ANOTHER_COMMON: 'another common value',
                CREDENTIALS: 'alice-dev-token', EXTRA_ALICE: 'extra dev alice', EXTRA_DEV_V: 'extra dev alice v']]
        dslDelegate.envPersonaValuesToMap('dev', 'Bob').should == [cliEnv: [
                COMMON: 'common value', ANOTHER_COMMON: 'another common value',
                CREDENTIALS: 'bob-dev-token', EXTRA_BOB: 'extra dev bob']]
        dslDelegate.envPersonaValuesToMap('beta', 'Alice').should == [cliEnv: [
                COMMON: 'common value', ANOTHER_COMMON: 'another common value',
                CREDENTIALS: 'alice-beta-token', EXTRA_ALICE: 'extra beta alice', EXTRA_BETA_V: 'extra beta alice v']]

        dslDelegate.envPersonaValuesToMap('dev', 'Unknown').should == [cliEnv: [
                COMMON: 'common value', ANOTHER_COMMON: 'another common value']]
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