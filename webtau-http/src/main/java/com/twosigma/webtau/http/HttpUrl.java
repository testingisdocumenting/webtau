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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HttpUrl {
    private HttpUrl() {
    }

    public static boolean isFull(String url) {
        int colonIdx = url.indexOf(':');
        int slashIdx = url.indexOf('/');

        return colonIdx != -1 && colonIdx < slashIdx;
    }

    public static String extractPath(String url) {
        if (!isFull(url)) {
            return url;
        }

        try {
            return new URL(url).getPath();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + url, e);
        }
    }

    public static Map<String, List<String>> extractQueryParams(String url) {
        String queryParams = isFull(url) ?
                extractQueryParamsFromFullUrl(url) :
                extractQueryParamsFromRelativeUrl(url);

        return queryParams == null ? Collections.emptyMap() : HttpQueryParamsParser.parseQueryParams(queryParams);
    }

    private static String extractQueryParamsFromRelativeUrl(String url) {
        int queryParamsStart = url.indexOf('?');
        if (queryParamsStart < 0) {
            return null;
        } else {
            return url.substring(queryParamsStart + 1);
        }
    }

    private static String extractQueryParamsFromFullUrl(String url) {
        try {
            return new URL(url).getQuery();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + url, e);
        }
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
