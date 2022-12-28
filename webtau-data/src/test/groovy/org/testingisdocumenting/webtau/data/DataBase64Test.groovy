/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.data

import org.junit.Test

import static org.testingisdocumenting.webtau.data.Data.data

class DataBase64Test {
    @Test
    void "encode text"() {
        String encoded = data.base64.encode("hello world")
        encoded.should == "aGVsbG8gd29ybGQ="
    }

    @Test
    void "decode text"() {
        String decoded = data.base64.decode("aGVsbG8gd29ybGQ=")
        decoded.should == "hello world"
    }
}
