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

package org.testingisdocumenting.webtau.cache

import org.junit.Test

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class CacheValueConverterTest {
    @Test
    void "should convert list of values"() {
        CacheValueConverter.convertToCached([100, "text", Paths.get("my-path"), "hello"]).should ==
                [100, 'text', ~/my-path/, 'hello']
    }

    @Test
    void "should convert map of values"() {
        CacheValueConverter.convertToCached([k1: 100, k2: "text", k3: Paths.get("my-path"), k4: "hello"]).should ==
                [k1: 100, k2: 'text', k3: ~/my-path/, k4: 'hello']
    }

    @Test
    void "should not allow to cache unsupported values"() {
        def expectedError = "unsupported cache type <class java.net.Socket>: Socket[unconnected]"

        code {
            CacheValueConverter.convertToCached(new Socket())
        } should throwException(expectedError)

        code {
            CacheValueConverter.convertToCached([100, "text", new Socket()])
        } should throwException(expectedError)

        code {
            CacheValueConverter.convertToCached([k1: 100, k2: "text", k3: new Socket()])
        } should throwException(expectedError)
    }

    @Test
    void "should only allow maps with string keys"() {
        code {
            CacheValueConverter.convertToCached([k1: 'v1', 100: 200, k3: 'v3'])
        } should throwException("can only cache maps with string keys\n" +
                "  given map: {k1=v1, 100=200, k3=v3}\n" +
                "  non string keys: 100")
    }
}
