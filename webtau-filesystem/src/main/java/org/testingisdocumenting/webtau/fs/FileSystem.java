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

package org.testingisdocumenting.webtau.fs;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.Task;
import org.testingisdocumenting.webtau.ant.UntarTask;
import org.testingisdocumenting.webtau.ant.UnzipTask;
import org.testingisdocumenting.webtau.cleanup.CleanupRegistration;
import org.testingisdocumenting.webtau.reporter.MessageToken;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg;
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class FileSystem {
    public static final FileSystem fs = new FileSystem();

    private FileSystem() {
    }

    public void unzip(Path src, Path dest) {
        unArchive("unzipping", "unzipped", UnzipTask::new, src, dest);
    }

    public void unzip(String src, Path dest) {
        unzip(Paths.get(src), dest);
    }

    public void unzip(String src, String dest) {
        unzip(Paths.get(src), Paths.get(dest));
    }

    public void untar(Path src, Path dest) {
        unArchive("untarring", "untarred", UntarTask::new, src, dest);
    }

    public void untar(String src, Path dest) {
        untar(Paths.get(src), dest);
    }

    public void untar(String src, String dest) {
        untar(Paths.get(src), Paths.get(dest));
    }

    public void copy(String src, Path dest) {
        copy(Paths.get(src), dest);
    }

    public void copy(String src, String dest) {
        copy(Paths.get(src), Paths.get(dest));
    }

    public void copy(Path src, Path dest) {
        WebTauStep step = WebTauStep.createStep(null,
                tokenizedMessage(action("copying"), urlValue(src.toString()), TO, urlValue(dest.toString())),
                (Object r) -> {
                    CopyResult result = (CopyResult) r;
                    return tokenizedMessage(action("copied"), classifier(result.type),
                            urlValue(result.fullSrc.toAbsolutePath().toString()), TO,
                            urlValue(result.fullDest.toAbsolutePath().toString()));
                },
                () -> copyImpl(src, dest));

        step.execute(StepReportOptions.REPORT_ALL);
    }

    public boolean exists(Path path) {
        return Files.exists(fullPath(path));
    }

    public boolean exists(String path) {
        return exists(Paths.get(path));
    }

    public Path createDir(String dir) {
        return createDir(Paths.get(dir));
    }

    public Path createDir(Path dir) {
        Path fullDirPath = fullPath(dir);

        WebTauStep step = WebTauStep.createStep(null,
                tokenizedMessage(action("creating"), classifier("dir"), urlValue(dir.toString())),
                () -> tokenizedMessage(action("created"), classifier("dir"), urlValue(fullDirPath.toAbsolutePath().toString())),
                () -> {
                    try {
                        Files.createDirectories(fullDirPath);
                        return fullDirPath;
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });

        return step.execute(StepReportOptions.REPORT_ALL);
    }

    /**
     * Deletes file or directory. In case of directory deletes all files inside
     * @param fileOrDir path to delete
     */
    public void delete(String fileOrDir) {
        delete(Paths.get(fileOrDir));
    }

    /**
     * Deletes file or directory. In case of directory deletes all files inside
     * @param fileOrDir path to delete
     */
    public void delete(Path fileOrDir) {
        Path fullFileOrDirPath = fullPath(fileOrDir);

        MessageToken classifier = classifier(classifierByPath(fullFileOrDirPath));
        WebTauStep step = WebTauStep.createStep(null,
                tokenizedMessage(action("deleting"), classifier, urlValue(fileOrDir.toString())),
                () -> tokenizedMessage(action("deleted"), classifier,
                        urlValue(fullFileOrDirPath.toAbsolutePath().toString())),
                () -> FileUtils.deleteQuietly(fullFileOrDirPath.toFile()));

        step.execute(StepReportOptions.REPORT_ALL);
    }

    public FileTextContent textContent(String path) {
        return textContent(Paths.get(path));
    }

    public FileTextContent textContent(Path path) {
        return new FileTextContent(fullPath(path));
    }

    public Path writeText(String path, String content) {
        return writeText(Paths.get(path), content);
    }

    public Path writeText(Path path, String content) {
        Path fullPath = fullPath(path);

        WebTauStep step = WebTauStep.createStep(null,
                tokenizedMessage(action("writing text content"), OF, classifier("size"),
                        numberValue(content.length()), TO, urlValue(path.toString())),
                () -> tokenizedMessage(action("wrote text content"), OF, classifier("size"),
                        numberValue(content.length()), TO, urlValue(fullPath.toString())),
                () -> {
                    try {
                        Files.write(fullPath, content.getBytes(StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });

        step.execute(StepReportOptions.REPORT_ALL);
        return fullPath;
    }

    public Path tempDir(String prefix) {
        return tempDir((Path) null, prefix);
    }

    public Path tempDir(String dir, String prefix) {
        return tempDir(getCfg().getWorkingDir().resolve(dir), prefix);
    }

    public Path tempDir(Path dir, String prefix) {
        WebTauStep step = WebTauStep.createStep(null,
                tokenizedMessage(action("creating temp directory with prefix"), urlValue(prefix)),
                (createdDir) -> tokenizedMessage(action("created temp directory"), urlValue(createdDir.toString())),
                () -> createTempDir(fullPath(dir), prefix));

        return step.execute(StepReportOptions.REPORT_ALL);
    }

    public Path fullPath(String relativeOrFull) {
        return fullPath(Paths.get(relativeOrFull));
    }

    public Path fullPath(Path relativeOrFull) {
        if (relativeOrFull == null) {
            return null;
        }

        if (relativeOrFull.isAbsolute()) {
            return relativeOrFull;
        }

        return getCfg().getWorkingDir().resolve(relativeOrFull).toAbsolutePath();
    }

    private void unArchive(String action, String actionCompleted,
                           BiFunction<Path, Path, Task> antTaskFactory, Path src, Path dest) {
        Path fullSrc = fullPath(src);
        Path fullDest = fullPath(dest);

        WebTauStep step = WebTauStep.createStep(null,
                tokenizedMessage(action(action), urlValue(src.toString()), TO, urlValue(dest.toString())),
                () -> tokenizedMessage(action(actionCompleted), urlValue(fullSrc.toString()), TO, urlValue(fullDest.toString())),
                () -> antTaskFactory.apply(fullSrc, fullDest).execute());

        step.execute(StepReportOptions.REPORT_ALL);
    }


    private static CopyResult copyImpl(Path src, Path dest) {
        Path fullSrc = fs.fullPath(src);
        Path fullDest = fs.fullPath(dest);

        try {
            if (Files.isDirectory(fullSrc) && Files.isDirectory(fullDest)) {
                FileUtils.copyDirectory(fullSrc.toFile(), fullDest.toFile());
                return new CopyResult("directory", fullSrc, fullDest);
            } else {
                Path dstForFile = Files.isDirectory(fullDest) ?
                        fullDest.resolve(fullSrc.getFileName()) :
                        fullDest;

                FileUtils.copyFile(fullSrc.toFile(), dstForFile.toFile());

                return new CopyResult("file", fullSrc, dstForFile);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static Path createTempDir(Path dir, String prefix) {
        try {
            if (dir != null) {
                Files.createDirectories(dir);
            }

            Path path = dir != null ? Files.createTempDirectory(dir, prefix) :
                    Files.createTempDirectory(prefix);

            CleanupRegistration.registerForCleanup("deleting", "deleted", "temp dir " + path,
                    () -> true,
                    () -> FileUtils.deleteQuietly(path.toFile()));

            return path.toAbsolutePath();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static String classifierByPath(Path path) {
        if (Files.isDirectory(path)) {
            return "dir";
        }

        if (Files.isSymbolicLink(path)) {
            return "symlink";
        }

        return "file";
    }

    static class CopyResult {
        private final String type;
        private final Path fullSrc;
        private final Path fullDest;

        public CopyResult(String type, Path fullSrc, Path fullDest) {
            this.type = type;
            this.fullSrc = fullSrc;
            this.fullDest = fullDest;
        }
    }
}
