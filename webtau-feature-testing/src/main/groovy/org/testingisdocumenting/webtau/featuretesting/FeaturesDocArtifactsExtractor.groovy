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

package org.testingisdocumenting.webtau.featuretesting

import groovy.transform.PackageScope
import org.testingisdocumenting.webtau.documentation.DocumentationArtifactsLocation
import org.testingisdocumenting.webtau.utils.FileUtils
import org.testingisdocumenting.webtau.utils.ResourceUtils
import org.testingisdocumenting.webtau.utils.StringUtils
import org.jsoup.Jsoup

import java.nio.file.Path
import java.nio.file.Paths

class FeaturesDocArtifactsExtractor {
    static String extractScenarioBody(String script, String scenario) {
        def scenarioIdx = script.indexOf(scenario)
        if (scenarioIdx == -1) {
            throw new RuntimeException("can't find scenario <${scenario}>")
        }

        def scopeStartIdx = script.indexOf("{", scenarioIdx)

        def nextScenarioIdx = script.indexOf('scenario', scopeStartIdx)
        if (nextScenarioIdx == -1) {
            nextScenarioIdx = script.length()
        }

        def scopeEndIdx = script.lastIndexOf("}", nextScenarioIdx)
        return StringUtils.stripIndentation(removeMarkedLines(script.substring(scopeStartIdx + 1, scopeEndIdx)))
    }

    static void extractCodeSnippets(String artifactName, String inputName, Map<String, String> scenarioToOutputFile) {
        def artifactsRoot = artifactsRoot(artifactName)

        def script = FileUtils.fileTextContent(Paths.get(inputName))

        scenarioToOutputFile.each { outputFileName, scenario ->
            def extracted = extractScenarioBody(script, scenario)
            FileUtils.writeTextContent(artifactsRoot.resolve(outputFileName), extracted)
        }
    }

    static void extractHtmlSnippets(String artifactName, String resourceName, Map<String, String> cssToOutputFile) {
        def artifactsRoot = artifactsRoot(artifactName)

        cssToOutputFile.each { outputFileName, css ->
            extractAndSaveHtml(resourceName, css,
                    artifactsRoot.resolve(outputFileName))
        }
    }

    @PackageScope
    static String extractHtml(String resourceName, String css) {
        def html = ResourceUtils.textContent(resourceName)
        return Jsoup.parse(html).select(css).html().toString()
    }

    private static Path artifactsRoot(String artifactName) {
        DocumentationArtifactsLocation.classBasedLocation(FeaturesDocArtifactsExtractor).
                resolve('doc-artifacts').resolve('snippets').resolve(artifactName)
    }

    private static void extractAndSaveHtml(String resourceName, String css, Path outputPath) {
        def html = extractHtml(resourceName, css)
        FileUtils.writeTextContent(outputPath, html)
    }

    private static String removeMarkedLines(String text) {
        def lines = text.split('\n')
        return lines.findAll { !it.contains(':remove from docs:')}.join('\n')
    }
}
