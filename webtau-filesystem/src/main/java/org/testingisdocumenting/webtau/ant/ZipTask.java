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

package org.testingisdocumenting.webtau.ant;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ZipTask extends Zip {
    public ZipTask(Path dir, Path dest) {
        setProject(new Project());
        getProject().init();

        try {
            Files.createDirectories(dest.getParent());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        setBasedir(dir.toAbsolutePath().toFile());
        setDestFile(dest.toAbsolutePath().toFile());
    }
}
