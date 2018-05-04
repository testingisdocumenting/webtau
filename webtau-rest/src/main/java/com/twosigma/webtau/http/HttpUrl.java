/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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
