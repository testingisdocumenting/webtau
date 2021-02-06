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

package org.testingisdocumenting.webtau.cache

import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.testingisdocumenting.webtau.time.DummyTimeProvider
import org.testingisdocumenting.webtau.time.Time
import org.testingisdocumenting.webtau.utils.JsonUtils

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.Executors

class FileBasedCacheTest {
    @After
    void cleanUp() {
        Time.setTimeProvider(null)
    }

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
    void "should persist values in the file as pretty print json if time passed since last put"() {
        def cacheFile = createTempCacheFile([:])

        Time.timeProvider = new DummyTimeProvider([0, 11_000])

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
    void "should not persist values in the file right away"() {
        def cacheFile = createTempCacheFile([:])

        Time.timeProvider = new DummyTimeProvider([0, 100])

        def fileBasedCache = new FileBasedCache({ -> cacheFile })
        fileBasedCache.put('accessToken', 'abc', 400)

        Assert.assertEquals("{ }", cacheFile.text)
    }

    @Test
    void "should create non expiring values if no expiration time is provided"() {
        def cacheFile = createTempCacheFile([:])

        def fileBasedCache = new FileBasedCache({ -> cacheFile })
        Time.timeProvider = new DummyTimeProvider([0, 11_000])
        fileBasedCache.put('accessToken', 'abc')

        Assert.assertEquals(String.format('{%n' +
                '  "accessToken" : {%n' +
                '    "value" : "abc",%n' +
                '    "expirationTime" : 9223372036854775807%n' +
                '  }%n' +
                '}'), cacheFile.text)
    }

    @Test
    void "should handle put from multiple threads"() {
        def cacheFile = createTempCacheFile([:])
        Time.timeProvider = new DummyTimeProvider(0)
        def fileBasedCache = new FileBasedCache({ -> cacheFile })

        def executor = Executors.newFixedThreadPool(300)

        def futures = []
        300.times {idx ->
            fileBasedCache.put("value" + idx, 0)

            futures << executor.submit {
                150.times {
                    fileBasedCache.put("value" + idx, fileBasedCache.get("value" + idx) + 1)
                }
            }
        }

        futures*.get()
        assert fileBasedCache.get("value0") == 150
        assert fileBasedCache.get("value1") == 150
        assert fileBasedCache.get("value9") == 150
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
