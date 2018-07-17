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

package com.twosigma.webtau.featuretesting

import com.twosigma.webtau.utils.FileUtils
import org.junit.Assert
import org.junit.Test

import java.nio.file.Paths

import static com.twosigma.webtau.featuretesting.FeaturesDocArtifactsExtractor.extractHtml
import static com.twosigma.webtau.featuretesting.FeaturesDocArtifactsExtractor.extractScenarioBody
import static org.junit.Assert.assertEquals

class FeaturesDocArtifactsExtractorTest {
    @Test
    void "extract script for documentation"() {
        def testPath = Paths.get('examples/scenarios.ui/waitTo.groovy')
        def script = FileUtils.fileTextContent(testPath)

        String scope = extractScenarioBody(script)
        assertEquals('search.open()\n\n' +
                'search.submit(query: "search this")\n' +
                'search.numberOfResults.waitTo == 2', scope)
    }

    @Test
    void "extract html snippet by css"() {
        def html = extractHtml("finders-and-filters.html", "#menu")
        assertEquals('<div id="menu"> \n' +
                ' <ul> \n' +
                '  <li> <a href="/book">book</a> </li> \n' +
                '  <li> <a href="/orders">orders</a> </li> \n' +
                '  <li> <a href="/help">help</a> </li> \n' +
                ' </ul> \n' +
                '</div>', html)
    }
}
