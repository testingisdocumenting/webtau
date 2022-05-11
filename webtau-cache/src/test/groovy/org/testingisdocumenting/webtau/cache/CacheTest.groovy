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

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.testingisdocumenting.webtau.cfg.WebTauConfig

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.cache.Cache.cache

class CacheTest {
    static Path cachePath

    @BeforeClass
    static void init() {
        cachePath = Files.createTempDirectory("webtau-cache")
        WebTauConfig.getCfg().getCachePathValue().set("test", cachePath)
    }

    @AfterClass
    static void cleanup() {
        cachePath.deleteDir()
    }

    @Test
    void "should throw assertion error when accessing value that is not present"() {
        code {
            cache.get("non-existing-cache-key")
        } should throwException("can't find cached value by key: non-existing-cache-key")
    }

    @Test
    void "should convert Path to string and back"() {
        def cachedValue = cache.value("path-to-string")
        cachedValue.set(Paths.get("dir-name"))

        cachedValue.get().should == ~/dir-name/
        (cachedValue.getAsPath() instanceof Path).should == true
        cachedValue.getAsPath().fileName.should == 'dir-name'
    }
}
