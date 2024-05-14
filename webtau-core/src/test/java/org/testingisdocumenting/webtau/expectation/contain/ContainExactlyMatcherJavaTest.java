/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.contain;

import org.junit.Test;
import org.testingisdocumenting.webtau.data.Person;

import java.util.List;

import static org.testingisdocumenting.webtau.Matchers.*;
import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.*;

public class ContainExactlyMatcherJavaTest {
    @Test
    public void match() {
        List<String> list = list("hello", "world", "of", "of", "testing");
        actual(list).should(containExactly("of", "world", "of", "hello", "testing"));
    }

    @Test
    public void matchListAsExpected() {
        List<String> list = list("hello", "world", "of", "of", "testing");
        actual(list).should(containExactly(list("of", "world", "of", "hello", "testing")));
    }

    @Test
    public void matchRecordsAndMaps() {
        // records-and-maps-example
        List<?> list = list(new Person("id1", 3, 10),
                new Person("id2", 4, 20),
                new Person("id2", 4, 20));

        actual(list).should(containExactly(
                map("id", "id2", "level", 4, "monthsAtCompany", 20),
                map("id", "id1", "level", 3, "monthsAtCompany", 10),
                map("id", "id2", "level", 4, "monthsAtCompany", 20)));
        // records-and-maps-example
    }

    @Test
    public void missingDuplicatedValue() {
        runExpectExceptionAndValidateOutput(AssertionError.class, """
                X failed expecting [value] to contain exactly ["of", "world", "of", "hello", "testing"]:
                    no matches found for: ["of"] (Xms)
                 \s
                  ["hello", "world", "of", "testing"]""", () -> {
            List<String> list = List.of("hello", "world", "of", "testing");
            actual(list).should(containExactly("of", "world", "of", "hello", "testing"));
        });
    }

    @Test
    public void missingDuplicatedValueRecordsAndMaps() {
        runExpectExceptionAndValidateOutput(AssertionError.class, """
                        X failed expecting [value] to contain exactly [
                                                                        {"id": "id2", "level": 4, "monthsAtCompany": 20},
                                                                        {"id": "id1", "level": 3, "monthsAtCompany": 10},
                                                                        {"id": "id2", "level": 4, "monthsAtCompany": 20}
                                                                      ]:
                            no matches found for: [{"id": "id2", "level": 4, "monthsAtCompany": 20}] (Xms)
                         \s
                          [{"id": "id1", "level": 3, "monthsAtCompany": 10}, {"id": "id2", "level": 4, "monthsAtCompany": 20}]""",
                () -> {
                    List<?> list = list(new Person("id1", 3, 10), new Person("id2", 4, 20));

                    actual(list).should(containExactly(
                            map("id", "id2", "level", 4, "monthsAtCompany", 20),
                            map("id", "id1", "level", 3, "monthsAtCompany", 10),
                            map("id", "id2", "level", 4, "monthsAtCompany", 20)));
                });
    }

    @Test
    public void suspectCandidateValueRecordsAndMaps() {
        runExpectExceptionCaptureAndValidateOutput(AssertionError.class, "contain-exactly-possible-mismatches-output", """
                X failed expecting [value] to contain exactly [
                                                                {"id": "id2", "level": 4, "monthsAtCompany": 20},
                                                                {"id": "id1", "level": 8, "monthsAtCompany": 10},
                                                                {"id": "id1", "level": 7, "monthsAtCompany": 12}
                                                              ]:
                    no matches found for: [{"id": "id1", "level": 8, "monthsAtCompany": 10}, {"id": "id1", "level": 7, "monthsAtCompany": 12}]
                    possible mismatches:
                   \s
                    [value][1].level:  actual: 4 <java.lang.Integer>
                                     expected: 8 <java.lang.Integer>
                    [value][0].level:  actual: 3 <java.lang.Integer>
                                     expected: 7 <java.lang.Integer> (Xms)
                 \s
                  [
                    {"id": "id1", "level": **3**, "monthsAtCompany": 12},
                    {"id": "id1", "level": **4**, "monthsAtCompany": 10},
                    {"id": "id2", "level": 4, "monthsAtCompany": 20}
                  ]""", () -> {

            // possible-mismatches-example
            List<?> list = list(
                    new Person("id1", 3, 12),
                    new Person("id1", 4, 10),
                    new Person("id2", 4, 20));

            actual(list).should(containExactly(
                    map("id", "id2", "level", 4, "monthsAtCompany", 20),
                    map("id", "id1", "level", 8, "monthsAtCompany", 10),
                    map("id", "id1", "level", 7, "monthsAtCompany", 12)));
            // possible-mismatches-example
        });
    }

    @Test
    public void suspectCandidateWithMissing() {
        runExpectExceptionAndValidateOutput(AssertionError.class, """
                X failed expecting [value] to contain exactly [
                                                                {"id": "id1", "level": 8, "monthsAtCompany": 10},
                                                                {"id": "id1", "level": 7, "monthsAtCompany": 12},
                                                                {"id": "id2", "level": 4, "monthsAtCompany": 20}
                                                              ]:
                    no matches found for: [{"id": "id1", "level": 8, "monthsAtCompany": 10}]
                    possible mismatches:
                   \s
                    [value][0].level:  actual: 5 <java.lang.Integer>
                                     expected: 8 <java.lang.Integer>
                   \s
                    missing values:
                   \s
                    [value][0].monthsAtCompany: 10 (Xms)
                 \s
                  [
                    {"id": "id1", "level": **5**, "monthsAtCompany": **<missing>**},
                    {"id": "id1", "level": 7, "monthsAtCompany": 12},
                    {"id": "id2", "level": 4, "monthsAtCompany": 20}
                  ]""", () -> {
            List<?> list = list(
                    map("id", "id1", "level", 5),
                    map("id", "id1", "level", 7, "monthsAtCompany", 12),
                    map("id", "id2", "level", 4, "monthsAtCompany", 20));

            actual(list).should(containExactly(
                    map("id", "id1", "level", 8, "monthsAtCompany", 10),
                    map("id", "id1", "level", 7, "monthsAtCompany", 12),
                    map("id", "id2", "level", 4, "monthsAtCompany", 20)));
        });
    }

    @Test
    public void suspectCandidateWithExtra() {
        runExpectExceptionAndValidateOutput(AssertionError.class, """
                X failed expecting [value] to contain exactly [
                                                                {"id": "id1", "level": 8, "monthsAtCompany": 10},
                                                                {"id": "id1", "level": 7, "monthsAtCompany": 12},
                                                                {"id": "id2", "level": 4, "monthsAtCompany": 20}
                                                              ]:
                    no matches found for: [{"id": "id1", "level": 8, "monthsAtCompany": 10}]
                    possible mismatches:
                   \s
                    [value][0].level:  actual: 5 <java.lang.Integer>
                                     expected: 8 <java.lang.Integer>
                    [value][0].monthsAtCompany:  actual: 14 <java.lang.Integer>
                                               expected: 10 <java.lang.Integer>
                   \s
                    extra values:
                   \s
                    [value][0].salary: "yes" (Xms)
                 \s
                  [
                    {"id": "id1", "level": **5**, "monthsAtCompany": **14**, "salary": **"yes"**},
                    {"id": "id1", "level": 7, "monthsAtCompany": 12},
                    {"id": "id2", "level": 4, "monthsAtCompany": 20}
                  ]""", () -> {
            List<?> list = list(
                    map("id", "id1", "level", 5, "monthsAtCompany", 14, "salary", "yes"),
                    map("id", "id1", "level", 7, "monthsAtCompany", 12),
                    map("id", "id2", "level", 4, "monthsAtCompany", 20));

            actual(list).should(containExactly(
                    map("id", "id1", "level", 8, "monthsAtCompany", 10),
                    map("id", "id1", "level", 7, "monthsAtCompany", 12),
                    map("id", "id2", "level", 4, "monthsAtCompany", 20)));
        });
    }

    @Test
    public void mismatchValue() {
        runExpectExceptionAndValidateOutput(AssertionError.class, """
                X failed expecting [value] to contain exactly ["of", "world", "hello", "sleeping"]:
                    no matches found for: ["sleeping"]
                    unexpected elements: ["testing"] (Xms)
                 \s
                  ["hello", "world", "of", **"testing"**]""", () -> {
            List<String> list = List.of("hello", "world", "of", "testing");
            actual(list).should(containExactly("of", "world", "hello", "sleeping"));
        });
    }

    @Test
    public void negativeMatch() {
        runAndValidateOutput(". [value] not contains exactly [\"of\", \"world\", \"hello\", \"testing\"] (Xms)", () -> {
            List<String> list = list("hello", "world", "of", "of", "testing");
            actual(list).shouldNot(containExactly("of", "world", "hello", "testing"));
        });
    }

    @Test
    public void negativeMismatch() {
        runExpectExceptionAndValidateOutput(AssertionError.class, """
                X failed expecting [value] to not contain exactly ["of", "world", "of", "hello", "testing"]:
                    contains exactly ["of", "world", "of", "hello", "testing"] (Xms)
                 \s
                  [**"hello"**, **"world"**, **"of"**, **"of"**, **"testing"**]""", () -> {
            List<String> list = List.of("hello", "world", "of", "of", "testing");
            actual(list).shouldNot(containExactly("of", "world", "of", "hello", "testing"));
        });
    }
}