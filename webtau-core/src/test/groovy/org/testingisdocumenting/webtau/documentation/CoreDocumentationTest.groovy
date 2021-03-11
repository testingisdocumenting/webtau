/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.documentation

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.reporter.StepReporter
import org.testingisdocumenting.webtau.reporter.StepReporters
import org.testingisdocumenting.webtau.reporter.WebTauStep
import org.testingisdocumenting.webtau.utils.FileUtils

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.WebTauCore.*

class CoreDocumentationTest implements StepReporter {
    List<String> allStepMessages = []
    List<String> failedStepMessages = []
    List<String> completedStepMessages = []

    @Before
    void initiateStepReporter() {
        allStepMessages.clear()
        StepReporters.add(this)
    }

    @After
    void unregisterStepReporter() {
        StepReporters.remove(this)
    }

    @Test
    void "captures Strings artifact into text file"() {
        doc.capture("text-artifact-id", "hello world")
        actual(FileUtils.fileTextContent(Paths.get("doc-artifacts/text-artifact-id.txt"))).should(
                equal("hello world"))

        actual(completedStepMessages[0]).should(
                equal("captured text documentation artifact text-artifact-id : doc-artifacts/text-artifact-id.txt"))
    }

    @Test
    void "captures map artifact into json file"() {
        doc.capture("json-artifact-id", [key: "hello world"])
        actual(FileUtils.fileTextContent(Paths.get("doc-artifacts/json-artifact-id.json"))).should(
                equal("{\n" +
                      "  \"key\" : \"hello world\"\n" +
                      "}"))

        actual(completedStepMessages[0]).should(
                equal("captured json documentation artifact json-artifact-id : doc-artifacts/json-artifact-id.json"))
    }

    @Test
    void "captures table artifact into csv file"() {
        def table = table("a", "b",
                          _______,
                           1,  2)

        doc.captureCsv("csv-artifact-id", table)
        actual(FileUtils.fileTextContent(Paths.get("doc-artifacts/csv-artifact-id.csv"))).should(
                equal("a,b\r\n" +
                      "1,2\r\n"))

        actual(completedStepMessages[0]).should(
                equal("captured csv documentation artifact csv-artifact-id : doc-artifacts/csv-artifact-id.csv"))
    }

    @Test
    void "should check artifact uniqueness"() {
        doc.capture("my-unique-id", "hello")

        code {
            doc.capture("my-unique-id", "hello")
        } should throwException("doc artifact name <my-unique-id.txt> was already used")
    }

    @Override
    void onStepStart(WebTauStep step) {
        allStepMessages << step.inProgressMessage.toString()
    }

    @Override
    void onStepSuccess(WebTauStep step) {
        allStepMessages << step.completionMessage.toString()
        completedStepMessages << step.completionMessage.toString()
    }

    @Override
    void onStepFailure(WebTauStep step) {
        allStepMessages << step.completionMessage.toString()
        failedStepMessages << step.completionMessage.toString()
    }
}
