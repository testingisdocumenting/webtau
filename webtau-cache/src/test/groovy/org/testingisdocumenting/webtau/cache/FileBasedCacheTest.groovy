/*
 * Copyright 2021 webtau maintainers
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
import org.junit.Test
import org.testingisdocumenting.webtau.time.Time
import org.testingisdocumenting.webtau.utils.FileUtils

import java.nio.file.Files
import java.nio.file.Path

class FileBasedCacheTest {
    @After
    void cleanUp() {
        Time.setTimeProvider(null)
    }

    @Test
    void "should load cached values from a provided file"() {
        def cacheDir = createTempCacheDir()
        FileUtils.writeTextContent(cacheDir.resolve('url.json'), '"http://test"')

        def fileBasedCache = new FileBasedCache({ -> cacheDir })
        fileBasedCache.get('url').should == 'http://test'
    }

    @Test
    void "should save cached value to a file within cache dir"() {
        def cacheDir = createTempCacheDir()

        def fileBasedCache = new FileBasedCache({ -> cacheDir })

        fileBasedCache.put('number', 200)
        fileBasedCache.get('number').should == 200

        FileUtils.fileTextContent(cacheDir.resolve('number.json')).should == '200'
    }

    @Test
    void "should load cache values on demand without caching"() {
        def cacheDir = createTempCacheDir()

        def fileBasedCache = new FileBasedCache({ -> cacheDir })

        fileBasedCache.put('number', 200)
        FileUtils.writeTextContent(cacheDir.resolve('number.json'), '300')

        fileBasedCache.get('number').should == 300
    }

    private static Path createTempCacheDir() {
        def cachePath = Files.createTempDirectory('webtau-cache')
        cachePath.toFile().deleteOnExit()

        return cachePath
    }
}
