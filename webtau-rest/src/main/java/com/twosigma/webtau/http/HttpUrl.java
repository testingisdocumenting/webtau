package com.twosigma.webtau.http;

import java.util.Arrays;
import java.util.List;

public class HttpUrl {
    private static final List<String> fullUrlPrefixes = Arrays.asList("http:", "https:", "file:", "mailto:");

    private HttpUrl() {
    }

    public static boolean isFull(String url) {
        return fullUrlPrefixes.stream().anyMatch(p -> url.toLowerCase().startsWith(p));
    }

    public static String concat(String left, String right) {
        if (left == null) {
            throw new IllegalArgumentException("passed url on the left is NULL");
        }

        if (right == null) {
            throw new IllegalArgumentException("passed url on the right is NULL");
        }

        if (left.endsWith("/") && !right.startsWith("/")) {
            return left + right;
        }

        if (! left.endsWith("/") && right.startsWith("/")) {
            return left + right;
        }

        if (left.endsWith("/") && right.startsWith("/")) {
            return left + right.substring(1);
        }

        return left + "/" + right;
    }
}
