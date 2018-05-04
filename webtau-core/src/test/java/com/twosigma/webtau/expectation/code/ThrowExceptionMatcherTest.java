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

package com.twosigma.webtau.expectation.code;

import org.junit.Test;

import java.util.regex.Pattern;

import static com.twosigma.webtau.Ddjt.code;
import static com.twosigma.webtau.Ddjt.throwException;

public class ThrowExceptionMatcherTest {
    @Test
    public void examples() {
        code(() -> {
            businessLogic(-10);
        }).should(throwException(IllegalArgumentException.class, "negative are not allowed"));

        code(() -> {
            businessLogic(-10);
        }).should(throwException(IllegalArgumentException.class, Pattern.compile("negative .* not allowed")));

        code(() -> {
            businessLogic(-10);
        }).should(throwException(IllegalArgumentException.class));
    }

    private static void businessLogic(int num) {
        if (num < 0) {
            throw new IllegalArgumentException("negative are not allowed");
        }
    }
}