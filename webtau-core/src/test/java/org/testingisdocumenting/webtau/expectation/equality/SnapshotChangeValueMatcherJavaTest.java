/*
 * Copyright 2024 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.equality;

import org.junit.Test;
import org.testingisdocumenting.webtau.testutils.TestConsoleOutput;

import static org.testingisdocumenting.webtau.Matchers.*;

public class SnapshotChangeValueMatcherJavaTest {
    @Test
    public void change() {
        var value = new SnapshotAwareDummyValue();
        value.takeSnapshot();

        value.doOperation();

        TestConsoleOutput.runCaptureAndValidateOutput("snapshot-should-change-output", """
                . [value] changed (Xms)""", () -> {
            // value-should-change
            actual(value).should(change);
            // value-should-change
        });
    }

    @Test
    public void waitChange() {
        var value = new SnapshotAwareDummyValue();
        value.takeSnapshot();
        value.enableAutoIncrement();

        TestConsoleOutput.runCaptureAndValidateOutput("snapshot-wait-to-change-output", """
                > waiting for [value] to change
                . [value] changed (Xms)""", () -> {
            // value-wait-to-change
            actual(value).waitTo(change);
            // value-wait-to-change
        });
    }

    @Test
    public void failWaitChange() {
        var value = new SnapshotAwareDummyValue();
        value.takeSnapshot();

        TestConsoleOutput.runExpectExceptionCaptureAndValidateOutput(AssertionError.class, "snapshot-wait-to-change-fail-output", """
                > waiting for [value] to change
                X failed waiting for [value] to change:
                    [value].name:  actual:     "name1" <java.lang.String>
                                 expected: not "name1" <java.lang.String>
                    [value].id:  actual:     "tid1" <java.lang.String>
                               expected: not "tid1" <java.lang.String> (Xms)""", () -> {
            actual(value).waitTo(change, 1, 10);
        });
    }

    @Test
    public void notChange() {
        var value = new SnapshotAwareDummyValue();
        value.takeSnapshot();

        // should-not-change
        actual(value).shouldNot(change);
        // should-not-change
    }

    @Test
    public void failToChange() {
        var value = new SnapshotAwareDummyValue();
        value.takeSnapshot();

        TestConsoleOutput.runExpectExceptionCaptureAndValidateOutput(AssertionError.class, "snapshot-change-fail-output", """
                X failed expecting [value] to change:
                    [value].name:  actual:     "name1" <java.lang.String>
                                 expected: not "name1" <java.lang.String>
                    [value].id:  actual:     "tid1" <java.lang.String>
                               expected: not "tid1" <java.lang.String> (Xms)""", () -> {

            actual(value).should(change);
        });
    }

    @Test
    public void failToNotChange() {
        var value = new SnapshotAwareDummyValue();
        value.takeSnapshot();
        value.doOperation();

        TestConsoleOutput.runExpectExceptionCaptureAndValidateOutput(AssertionError.class, "snapshot-not-change-fail-output", """
                X failed expecting [value] to not change:
                    [value].name:  actual: "name1-changed" <java.lang.String>
                                 expected: "name1" <java.lang.String>
                                                 ^ (Xms)""", () -> {
            actual(value).shouldNot(change);
        });
    }
}