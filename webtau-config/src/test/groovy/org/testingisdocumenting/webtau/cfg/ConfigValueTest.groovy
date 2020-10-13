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

package org.testingisdocumenting.webtau.cfg

import org.junit.Test

import static org.testingisdocumenting.webtau.WebTauCore.persona

class ConfigValueTest {
    @Test
    void "takes the most recently set value"() {
        def configValue = ConfigValue.declare("vk", "description", { -> "dv" })
        configValue.set("config-file", "file value")
        configValue.set("command-line", "command line")

        configValue.getAsString().should == "command line"
    }

    @Test
    void "converts string value true to boolean true"() {
        def configValue = ConfigValue.declare("headless", "headless", { -> false })
        configValue.getAsBoolean().should == false

        configValue.set("command-line", "True")
        configValue.getAsBoolean().should == true

        configValue.set("another", "true")
        configValue.getAsBoolean().should == true
    }

    @Test
    void "initialize its value by looking through provided map of values using original and prefixed uppercase key"() {
        def configValue = ConfigValue.declare("pathToThat", "path", null)
        configValue.accept("env vars", [dummy: 1, WEBTAU_PATH_TO_THAT: "path1"])
        configValue.getAsString().should == "path1"

        configValue.accept("command line", [dummy: 1, pathToThat: "path2"])
        configValue.getAsString().should == "path2"
    }

    @Test
    void "set and get the value based current persona"() {
        def John = persona('Jonh')
        def Bob = persona('Bob')

        def configValue = ConfigValue.declare("vk", "description", { -> "dv" })

        configValue.isDefault().should == true
        configValue.getAsString().should == 'dv'

        John {
            configValue.isDefault().should == true
            configValue.getAsString().should == 'dv'
        }

        configValue.set("manual", "new-value")

        John {
            configValue.isDefault().should == false
            configValue.getAsString().should == 'new-value'
        }

        Bob {
            configValue.isDefault().should == false
            configValue.getAsString().should == 'new-value'

            configValue.set("manual", "new-Bob-value")
        }

        configValue.getAsString().should == 'new-value'

        Bob {
            configValue.isDefault().should == false
            configValue.getAsString().should == 'new-Bob-value'
        }

        John {
            configValue.isDefault().should == false
        }
    }
}
