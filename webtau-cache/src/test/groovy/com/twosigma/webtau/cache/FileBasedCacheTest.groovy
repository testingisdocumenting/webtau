/*
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

package com.twosigma.webtau.cache

import com.twosigma.webtau.utils.JsonUtils
import org.junit.Assert
import org.junit.Test

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class FileBasedCacheTest {
    @Test
    void "should load cached values from a provided file"() {
        def cache = [
                url: [
                        value: 'http://test',
                        expirationTime: Long.MAX_VALUE
                ],
                cost: [
                        value: 100,
                        expirationTime: Long.MAX_VALUE
                ],
                expired: [
                        value: 'expired',
                        expirationTime: 100
                ]
        ]

        def cacheFile = createTempCacheFile(cache)
        def fileBasedCache = new FileBasedCache({ -> cacheFile })
        assert fileBasedCache.get('url') == 'http://test'
        assert fileBasedCache.get('cost') == 100
        assert fileBasedCache.get('nonExisting') == null
        assert fileBasedCache.get('expired') == null
    }

    @Test
    void "should persist values in the file as pretty print json"() {
        def cacheFile = createTempCacheFile([:])
        def fileBasedCache = new FileBasedCache({ -> cacheFile })
        fileBasedCache.put('accessToken', 'abc', 400)

        Assert.assertEquals(String.format('{%n' +
                '  "accessToken" : {%n' +
                '    "value" : "abc",%n' +
                '    "expirationTime" : 400%n' +
                '  }%n' +
                '}'), cacheFile.text)
    }

    @Test
    void "should create non expiring values if no expiration time is provided"() {
        def cacheFile = createTempCacheFile([:])
        def fileBasedCache = new FileBasedCache({ -> cacheFile })
        fileBasedCache.put('accessToken', 'abc')

        Assert.assertEquals(String.format('{%n' +
                '  "accessToken" : {%n' +
                '    "value" : "abc",%n' +
                '    "expirationTime" : 9223372036854775807%n' +
                '  }%n' +
                '}'), cacheFile.text)
    }

    @Test
    void "should handle non existing file"() {
        def cachePath = Paths.get('nonExisting.json')
        cachePath.toFile().deleteOnExit()
        def cache = new FileBasedCache({ -> cachePath })

        assert cache.get('key') == null

        cache.put('key', 'value')
        assert cache.get('key') == 'value'
    }

    private static Path createTempCacheFile(Map<String, Object> content) {
        def cachePath = Files.createTempFile('webtau.cache', '.json')
        cachePath.text = JsonUtils.serializePrettyPrint(content)
        cachePath.toFile().deleteOnExit()

        return cachePath
    }
}
