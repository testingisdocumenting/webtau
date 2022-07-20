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

package org.testingisdocumenting.webtau.utils

import org.junit.Test

import java.nio.charset.StandardCharsets
import java.nio.file.Files

class FileUtilsTest {
    @Test
    void "delete dir with sub dirs and files"() {
        def root = Files.createTempDirectory("znai_test")
        Files.createDirectory(root.resolve("d1"))
        Files.createDirectory(root.resolve("d2"))

        FileUtils.writeTextContent(root.resolve("d1").resolve("file1.txt"), "hello")
        FileUtils.writeTextContent(root.resolve("d2").resolve("file2.txt"), "hello")

        FileUtils.deleteFileOrDirQuietly(root)

        assert !Files.exists(root)
    }

    @Test
    void "delete file"() {
        def root = Files.createTempDirectory("znai_test")
        def file1 = root.resolve("file1.txt")

        FileUtils.writeTextContent(file1, "hello")

        FileUtils.deleteFileOrDirQuietly(file1)
        assert !Files.exists(file1)

        FileUtils.deleteFileOrDirQuietly(root)
        assert !Files.exists(root)
    }

    @Test
    void "should read text content from a file"() {
        def testFile = new File("dummy.txt")
        testFile.deleteOnExit()

        Files.write(testFile.toPath(), "content of a file \u275e".getBytes(StandardCharsets.UTF_8))
        assert FileUtils.fileTextContent(testFile.toPath()) == "content of a file ❞"
    }
}
