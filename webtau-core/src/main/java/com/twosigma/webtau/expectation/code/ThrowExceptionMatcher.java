/*
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

package com.twosigma.webtau.expectation.code;

import static com.twosigma.webtau.Ddjt.createActualPath;

import com.twosigma.webtau.expectation.CodeBlock;
import com.twosigma.webtau.expectation.CodeMatcher;
import com.twosigma.webtau.expectation.equality.CompareToComparator;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.regex.Pattern;

public class ThrowExceptionMatcher implements CodeMatcher {
    private String expectedMessage;
    private Pattern expectedMessageRegexp;
    private Class expectedClass;
    private String thrownMessage;
    private Class thrownClass;
    private CompareToComparator comparator;

    public ThrowExceptionMatcher(String expectedMessage) {
        this.expectedMessage = expectedMessage;
    }

    public ThrowExceptionMatcher(Pattern expectedMessageRegexp) {
        this.expectedMessageRegexp = expectedMessageRegexp;
    }

    public ThrowExceptionMatcher(Class expectedClass) {
        this.expectedClass = expectedClass;
    }

    public ThrowExceptionMatcher(Class expectedClass, Pattern expectedMessageRegexp) {
        this.expectedClass = expectedClass;
        this.expectedMessageRegexp = expectedMessageRegexp;
    }

    public ThrowExceptionMatcher(Class expectedClass, String expectedMessage) {
        this.expectedClass = expectedClass;
        this.expectedMessage = expectedMessage;
    }

    @Override
    public String matchingMessage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String matchedMessage(CodeBlock codeBlock) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String mismatchedMessage(CodeBlock codeBlock) {
        return comparator.generateEqualMismatchReport();
    }

    @Override
    public boolean matches(CodeBlock codeBlock) {
        try {
            codeBlock.execute();
        } catch (Throwable t) {
            extractExceptionDetails(t);
        }

        comparator = CompareToComparator.comparator();

        boolean isEqual = true;

        if (expectedMessage != null) {
            isEqual = comparator.compareIsEqual(createActualPath("expected exception message"), thrownMessage, expectedMessage);
        }

        if (expectedMessageRegexp != null) {
            isEqual =  comparator.compareIsEqual(createActualPath("expected exception message"),
                    thrownMessage, expectedMessageRegexp) && isEqual;
        }

        if (expectedClass != null) {
            isEqual = comparator.compareIsEqual(createActualPath("expected exception class"),
                    thrownClass, expectedClass) && isEqual;
        }

        return isEqual;
    }

    private void extractExceptionDetails(Throwable t) {
        if (t instanceof UndeclaredThrowableException) {
            Throwable undeclaredCheckedException = t.getCause();
            thrownMessage = undeclaredCheckedException.getMessage();
            thrownClass = undeclaredCheckedException.getClass();
        } else {
            thrownMessage = t.getMessage();
            thrownClass = t.getClass();
        }
    }
}
