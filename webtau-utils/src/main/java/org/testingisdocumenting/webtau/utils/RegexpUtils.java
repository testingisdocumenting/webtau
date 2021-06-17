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

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexpUtils {
    public static class ReplaceResultWithMeta {
        private final String result;
        private final int numberOfMatches;

        private ReplaceResultWithMeta(String result, int numberOfMatches) {
            this.result = result;
            this.numberOfMatches = numberOfMatches;
        }

        public String getResult() {
            return result;
        }

        public int getNumberOfMatches() {
            return numberOfMatches;
        }
    }

    private RegexpUtils() {
    }

    public static String replaceAll(String source, Pattern regexp, Function<Matcher, String> replacement) {
        return replaceAllAndCount(source, regexp, replacement).result;
    }

    public static ReplaceResultWithMeta replaceAllAndCount(String source, Pattern regexp, String replacement) {
        Matcher matcher = regexp.matcher(source);

        boolean result = matcher.find();
        if (!result) {
            return new ReplaceResultWithMeta(source, 0);
        }

        int count = 0;
        StringBuffer sb = new StringBuffer();

        do {
            count++;
            matcher.appendReplacement(sb, replacement);
            result = matcher.find();
        } while (result);

        matcher.appendTail(sb);

        return new ReplaceResultWithMeta(sb.toString(), count);
    }

    public static ReplaceResultWithMeta replaceAllAndCount(String source, Pattern regexp, Function<Matcher, String> replacement) {
        Matcher matcher = regexp.matcher(source);
        StringBuffer result = new StringBuffer();

        int count = 0;
        while (matcher.find()) {
            matcher.appendReplacement(result, replacement.apply(matcher));
            count++;
        }

        matcher.appendTail(result);

        return new ReplaceResultWithMeta(result.toString(), count);
    }

    public static String extractByRegexp(String source, String regexp) {
        Pattern pattern = Pattern.compile(regexp);
        return extractByRegexp(source, pattern);
    }

    public static String extractByRegexp(String source, Pattern pattern) {
        Matcher matcher = pattern.matcher(source);
        boolean found = matcher.find();
        if (!found) {
            return null;
        }

        return matcher.group(1);
    }
}
