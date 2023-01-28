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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UrlUtils {
    private static final String HTTPS_URL_PREFIX = "https://";
    private static final String HTTP_URL_PREFIX = "http://";

    private static final Pattern ROUTE_CHARS_TO_ESCAPE = Pattern.compile("([<(\\[^\\-=\\\\$!|\\])?*+.>])");
    private static final Pattern ROUTE_NAMED_PARAM_REGEXP_CURLY = Pattern.compile("\\{(\\w+)}");
    private static final Pattern ROUTE_NAMED_PARAM_REGEXP_COLON = Pattern.compile(":(\\w+)");

    private UrlUtils() {
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

    public static int extractPort(String url) {
        if (url.isEmpty()) {
            return -1;
        }

        int httpIdx = url.indexOf(HTTP_URL_PREFIX);
        int httpsIdx = url.indexOf(HTTPS_URL_PREFIX);

        if (httpIdx == -1 && httpsIdx == -1) {
            return -1;
        }

        int colonStartIdx = httpIdx == -1 ?
                HTTPS_URL_PREFIX.length() :
                HTTP_URL_PREFIX.length();

        int colonIdx = url.indexOf(':', colonStartIdx);
        if (colonIdx == -1) {
            return url.startsWith("https:") ? 443 :
                    url.startsWith("http:") ? 80 : -1;
        }

        int slashIdx = url.indexOf('/', colonIdx);
        if (slashIdx == -1) {
            String portAsText = url.substring(colonIdx + 1);
            return StringUtils.convertToNumber(portAsText).intValue();
        }

        String portAsText = url.substring(colonIdx + 1, slashIdx);
        return StringUtils.convertToNumber(portAsText).intValue();
    }

    public static Map<String, List<String>> parseQueryParams(String queryParams) {
        String[] params = queryParams.split("&");

        return Arrays.stream(params)
                .map(p -> p.split("="))
                .collect(Collectors.groupingBy(UrlUtils::extractQueryKey,
                        Collectors.mapping(UrlUtils::extractQueryValue, Collectors.toList())));
    }

    public static Map<String, List<String>> extractQueryParams(String url) {
        String queryParams = isFull(url) ?
                extractQueryParamsFromFullUrl(url) :
                extractQueryParamsFromRelativeUrl(url);

        return queryParams == null ? Collections.emptyMap() : UrlUtils.parseQueryParams(queryParams);
    }

    public static String concat(URI uri, String right) {
        if (uri == null) {
            throw new IllegalArgumentException("passed uri is NULL");
        }

        return concat(uri.toString(), right);
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

        if (!left.endsWith("/") && right.startsWith("/")) {
            return left + right;
        }

        if (left.endsWith("/") && right.startsWith("/")) {
            return left + right.substring(1);
        }

        return left + "/" + right;
    }

    public static UrlRouteRegexp buildRouteRegexpAndGroupNames(String pathDefinition) {
        Set<String> groupNames = new LinkedHashSet<>();

        String escaped = RegexpUtils.replaceAll(pathDefinition, ROUTE_CHARS_TO_ESCAPE, (m) -> {
            String name = m.group(1);
            return "\\\\" + name;
        });

        Function<Matcher, String> matcherFunc = (m) -> {
            String name = m.group(1);
            groupNames.add(name);

            return "(?<" + name + ">\\\\w+)";
        };

        String curlyReplaced = RegexpUtils.replaceAll(escaped, ROUTE_NAMED_PARAM_REGEXP_CURLY, matcherFunc);
        String colonReplaced = RegexpUtils.replaceAll(curlyReplaced, ROUTE_NAMED_PARAM_REGEXP_COLON, matcherFunc);

        return new UrlRouteRegexp(Pattern.compile(colonReplaced), groupNames);
    }

    public static String removeTrailingSlash(String url) {
        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        }

        return url;
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

    private static String extractQueryKey(String[] splitParam) {
        String key = splitParam[0];

        int openingBraceIdx = key.indexOf('[');
        if (openingBraceIdx >= 0 && key.contains("]")) {
            key = key.substring(0, openingBraceIdx);
        }

        return key;
    }

    private static String extractQueryValue(String[] splitParam) {
        return splitParam.length == 1 ? null : splitParam[1];
    }
}
