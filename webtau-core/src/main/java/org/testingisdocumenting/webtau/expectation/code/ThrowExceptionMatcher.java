/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.code;

import static org.testingisdocumenting.webtau.WebTauCore.createActualPath;

import org.testingisdocumenting.webtau.expectation.ActualValueAware;
import org.testingisdocumenting.webtau.expectation.CodeBlock;
import org.testingisdocumenting.webtau.expectation.CodeMatcher;
import org.testingisdocumenting.webtau.expectation.ExpectedValuesAware;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.reporter.stacktrace.StackTraceUtils;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ThrowExceptionMatcher implements CodeMatcher, ExpectedValuesAware, ActualValueAware {
    private String expectedMessage;
    private Pattern expectedMessageRegexp;
    private Class<?> expectedClass;
    private String thrownMessage;
    private Class<?> thrownClass;
    private CompareToComparator comparator;
    private String thrownExceptionStackTrace;

    public ThrowExceptionMatcher(String expectedMessage) {
        this.expectedMessage = expectedMessage;
    }

    public ThrowExceptionMatcher(Pattern expectedMessageRegexp) {
        this.expectedMessageRegexp = expectedMessageRegexp;
    }

    public ThrowExceptionMatcher(Class<?> expectedClass) {
        this.expectedClass = expectedClass;
    }

    public ThrowExceptionMatcher(Class<?> expectedClass, Pattern expectedMessageRegexp) {
        this.expectedClass = expectedClass;
        this.expectedMessageRegexp = expectedMessageRegexp;
    }

    public ThrowExceptionMatcher(Class<?> expectedClass, String expectedMessage) {
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
        return comparator.generateEqualMismatchReport() +
                (thrownExceptionStackTrace != null ? "\nstack trace:\n" + thrownExceptionStackTrace : "");
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
        thrownExceptionStackTrace = StackTraceUtils.renderStackTrace(t);

        if (t instanceof UndeclaredThrowableException) {
            Throwable undeclaredCheckedException = t.getCause();
            thrownMessage = undeclaredCheckedException.getMessage();
            thrownClass = undeclaredCheckedException.getClass();
        } else if (t instanceof ExceptionInInitializerError) {
            Throwable exception = ((ExceptionInInitializerError) t).getException();
            thrownMessage = exception.getMessage();
            thrownClass = exception.getClass();
        } else {
            thrownMessage = t.getMessage();
            thrownClass = t.getClass();
        }
    }

    @Override
    public Stream<Object> expectedValues() {
        if (expectedMessage != null) {
            return Stream.of(expectedMessage);
        }

        if (expectedClass != null) {
            return Stream.of(expectedClass);
        }

        return Stream.empty();
    }

    @Override
    public Object actualValue() {
        return thrownMessage;
    }
}
