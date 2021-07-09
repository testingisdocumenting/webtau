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

package org.testingisdocumenting.webtau.server.route

import org.junit.Test

import java.util.regex.Pattern

class RouteParamsParserTest {
    @Test
    void "match by path definition"() {
//        def parser = new RouteParamsParser("/my/^super-path/{id}/hello/{name}")
//        parser.matches("/my/su-per.path/3433/hello/name")

        def pattern = Pattern.compile("/my/super-path/(<id>\\{\\w+\\\n})/hello/(<name>\\{\\w+\\})")
        def found = pattern.matcher("/my/super-path/{id}/hello/{name}").find()
        println found

    }
}