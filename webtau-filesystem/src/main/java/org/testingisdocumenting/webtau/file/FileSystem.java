/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.file;

import org.apache.commons.io.FileUtils;
import org.testingisdocumenting.webtau.file.zip.UnzipTask;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.TestStep;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg;
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class FileSystem {
    public static final FileSystem fs = new FileSystem();

    private FileSystem() {
    }

    public void unzip(Path src, Path dest) {
        TestStep<Object, Void> step = TestStep.createStep(null,
                tokenizedMessage(action("unzipping "), urlValue(src.toString()), TO, urlValue(dest.toString())),
                () -> tokenizedMessage(action("unzipped "), urlValue(src.toString()), TO, urlValue(dest.toString())),
                () -> {
                    UnzipTask unzipTask = new UnzipTask(fullPath(src), fullPath(dest));
                    unzipTask.execute();
                });

        step.execute(StepReportOptions.REPORT_ALL);
    }

    public void unzip(String src, Path dest) {
        unzip(Paths.get(src), dest);
    }

    public void unzip(String src, String dest) {
        unzip(Paths.get(src), Paths.get(dest));
    }

    public Path tempDir(String prefix) {
        try {
            Path path = Files.createTempDirectory(prefix);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> FileUtils.deleteQuietly(path.toFile())));

            return path;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Path fullPath(Path relativeOrFull) {
        if (relativeOrFull.isAbsolute()) {
            return relativeOrFull;
        }

        return getCfg().getWorkingDir().resolve(relativeOrFull);
    }
}
