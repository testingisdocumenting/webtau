package com.twosigma.webtau.utils;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexpUtils {
    private RegexpUtils() {
    }

    public static String replaceAll(String source, Pattern regexp, Function<Matcher, String> replacement) {
        Matcher matcher = regexp.matcher(source);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(result, replacement.apply(matcher));
        }

        matcher.appendTail(result);

        return result.toString();
    }
}
