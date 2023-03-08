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

package org.testingisdocumenting.webtau.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {
    private FileUtils() {
    }

    /**
     * delete dir with sub dirs and files, ignoring errors.
     * Not using apache as we delete dirs on exit,
     * and by the time maven exits (in case of maven plugin), apache io is unloaded already
     * @param path dir to delete
     */
    public static void deleteFileOrDirQuietly(Path path) {
        try (Stream<Path> walk = Files.walk(path)) {
            walk.sorted(Comparator.reverseOrder())
                    .forEach(FileUtils::deleteQuietly);
        } catch (IOException e) {
            // ignored
        }
    }

    public static void createDirsForFile(Path path) {
        Path parent = path.toAbsolutePath().getParent();
        if (parent == null) {
            return;
        }

        createDirs(parent);
    }

    public static void createDirs(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String extractNameWithoutExtension(Path file) {
        String name = file.getFileName().toString();
        int dotIdx = name.indexOf('.');
        return dotIdx == -1 ? name : name.substring(0, dotIdx);
    }

    public static String extractExtension(Path file) {
        String name = file.getFileName().toString();
        int dotIdx = name.lastIndexOf('.');
        return dotIdx == -1 ? "" : name.substring(dotIdx + 1);
    }

    public static void writeTextContent(Path path, String text) {
        writeBinaryContent(path, text.getBytes());
    }

    public static void writeBinaryContent(Path path, byte[] content) {
        try {
            createDirsForFile(path);
            Files.write(path, content);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String fileTextContent(Path path) {
        if (!Files.exists(path)) {
            throw new RuntimeException(path.toAbsolutePath() + " doesn't exist");
        }

        try {
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static byte[] fileBinaryContent(Path path) {
        if (!Files.exists(path)) {
            throw new RuntimeException(path.toAbsolutePath() + " doesn't exist");
        }

        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void deleteQuietly(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            // ignored
        }
    }

    public static Path existingPathOrThrow(Path... paths) {
        List<Path> nonNull = Arrays.stream(paths).filter(Objects::nonNull).collect(Collectors.toList());

        return nonNull.stream().filter(Files::exists).findFirst().orElseThrow(() ->
                new RuntimeException("can't find any of the following files:\n" +
                        nonNull.stream().map(Path::toString).collect(Collectors.joining("\n"))));
    }
}
