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

package org.testingisdocumenting.webtau.featuretesting

import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.console.ansi.Color
import org.testingisdocumenting.webtau.expectation.ActualPath
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator
import org.testingisdocumenting.webtau.utils.FileUtils
import org.testingisdocumenting.webtau.utils.JsonUtils

import java.nio.file.Files
import java.nio.file.Paths
import java.util.function.Function

class WebTauEndToEndTestValidator {
    private static final String RUN_DETAILS_FILE_NAME = 'run-details'
    private static final String EXPECTATIONS_DIR_NAME = 'test-expectations'

    private WebTauEndToEndTestValidator() {
    }

    static void validateAndSaveTestDetails(String testName, Map testDetails, Function resultConverter = { v -> v }) {
        def expectedPath = Paths.get(EXPECTATIONS_DIR_NAME)
                .resolve(testName).resolve(RUN_DETAILS_FILE_NAME + '.json')
        def actualPath = Paths.get(EXPECTATIONS_DIR_NAME)
                .resolve(testName).resolve(RUN_DETAILS_FILE_NAME + '.actual.json')

        def serializedTestDetails = JsonUtils.serializePrettyPrint(resultConverter.apply(testDetails))

        if (! Files.exists(expectedPath)) {
            FileUtils.writeTextContent(expectedPath, serializedTestDetails)

            throw new AssertionError('make sure ' + expectedPath + ' is correct. and commit it as a baseline. ' +
                    'test will not fail next time unless output of the test is changed')
        }

        def expectedDetails = resultConverter.apply(JsonUtils.deserializeAsMap(
                FileUtils.fileTextContent(expectedPath)))

        CompareToComparator comparator = CompareToComparator.comparator()
        def isEqual = comparator.compareIsEqual(new ActualPath('testDetails'), testDetails, expectedDetails)

        if (! isEqual) {
            ConsoleOutputs.out('reports are different, you can use IDE to compare files: ', Color.PURPLE, actualPath,
                    Color.BLUE, ' and ', Color.PURPLE, expectedPath)
            FileUtils.writeTextContent(actualPath, serializedTestDetails)
            throw new AssertionError(comparator.generateEqualMismatchReport())
        } else {
            Files.deleteIfExists(actualPath)
        }
    }
}
