package com.twosigma.webtau.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileUtils {
    private FileUtils() {
    }

    public static void writeTextContent(Path path, String text) {
        try {
            Path parent = path.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.write(path, text.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String fileTextContent(Path path) {
        if (!Files.exists(path)) {
            throw new RuntimeException(path.toAbsolutePath() + " doesn't exist");
        }

        try {
            return Files.lines(path).collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] fileBinaryContent(Path path) {
        if (!Files.exists(path)) {
            throw new RuntimeException(path.toAbsolutePath() + " doesn't exist");
        }

        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Path existingPathOrThrow(Path... paths) {
        List<Path> nonNull = Arrays.stream(paths).filter(Objects::nonNull).collect(Collectors.toList());

        return nonNull.stream().filter(p -> Files.exists(p)).findFirst().orElseThrow(() ->
                new RuntimeException("can't find any of the following files:\n" +
                        nonNull.stream().map(Path::toString).collect(Collectors.joining("\n"))));
    }
}
