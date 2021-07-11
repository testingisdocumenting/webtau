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

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class RouteParamsParserTest {
    @Test
    void "match by path definition"() {
        def parser = new RouteParamsParser("/my/^super-path/{id}/hello/{name}")
        def url = "/my/^super-path/3433/hello/bob"

        parser.matches(url).should == true
        parser.groupNames.should == ["id", "name"]

        def params = parser.parse(url)
        params.get("id").should == "3433"
        params.get("name").should == "bob"
    }

    @Test
    void "does not match path"() {
        def parser = new RouteParamsParser("/my/^super-path/{id}/hello/{name}")
        def url = "/my/^super-path/3433/hello"

        parser.matches(url).should == false
        parser.groupNames.should == ["id", "name"]

        code {
            parser.parse(url)
        } should throwException("url </my/^super-path/3433/hello> does not match pattern </my/\\^super\\-path/(?<id>\\w+)/hello/(?<name>\\w+)>")
    }
}