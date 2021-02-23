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

import org.apache.ant.compress.taskdefs.ExpandBase;
import org.apache.tools.ant.Project;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

class UnArchiveTaskSetup {
    static void setup(ExpandBase task, Path src, Path dest) {
        task.setProject(new Project());
        task.getProject().init();

        try {
            Files.createDirectories(dest);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        task.setSrc(src.toAbsolutePath().toFile());
        task.setDest(dest.toAbsolutePath().toFile());
        task.setOverwrite(true);
    }
}
