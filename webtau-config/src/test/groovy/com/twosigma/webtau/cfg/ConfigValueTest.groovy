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

package com.twosigma.webtau.cfg

import org.junit.Test

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
}
