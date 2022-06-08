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

package scenarios.fs

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("create temp dir") {
    // temp-dir
    def dir = fs.tempDir("my-dir-prefix")
    def path = dir.resolve("my-file")
    fs.writeText(path, "hello world")
    // temp-dir
}

scenario("create temp dir inside dir") {
    // temp-dir-parent
    def parentDir = fs.tempDir("my-dir-prefix")
    def dir = fs.tempDir(parentDir, "nested-temp-dir")
    // temp-dir-parent
    def path = dir.resolve("my-file")
    fs.writeText(path, "hello world")
}

scenario("create temp file") {
    // temp-file
    def file = fs.tempFile("my-file-prefix", ".txt")
    fs.writeText(file, "hello world")
    // temp-file
}

scenario("create temp file inside dir") {
    // temp-file-parent
    def dir = fs.tempDir("temp-dir-prefix")
    def file = fs.tempFile(dir, "my-file-prefix", ".txt")
    // temp-file-parent

    fs.writeText(file, "hello world")
    doc.capture("temp-file-path", file.toAbsolutePath().toString())
}
