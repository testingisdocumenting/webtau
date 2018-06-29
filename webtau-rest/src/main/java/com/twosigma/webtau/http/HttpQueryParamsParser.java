package com.twosigma.webtau.http;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpQueryParamsParser {
    public static Map<String, List<String>> parseQueryParams(String queryParams) {
        String[] params = queryParams.split("&");

        return Arrays.stream(params)
                .map(p -> p.split("="))
                .collect(Collectors.groupingBy(HttpQueryParamsParser::extractQueryKey,
                        Collectors.mapping(HttpQueryParamsParser::extractQueryValue, Collectors.toList())));
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
