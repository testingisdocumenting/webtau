/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.data;

import org.apache.commons.lang3.StringUtils;
import org.testingisdocumenting.webtau.cfg.WebTauConfig;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.utils.FileUtils;
import org.testingisdocumenting.webtau.utils.ResourceUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;

class DataContentUtils {
    private DataContentUtils() {
    }

    @SuppressWarnings("unchecked")
    static <R> R handleDataTextContent(String dataType, String fileOrResourcePath, Function<String, R> convertor) {
        WebTauStep step = WebTauStep.createStep(
                null,
                tokenizedMessage(action("reading"), classifier(dataType), FROM, classifier("file or resource"), stringValue(fileOrResourcePath)),
                (result) -> {
                    ContentResult contentResult = (ContentResult) result;
                    return tokenizedMessage(action("read"), numberValue(contentResult.numberOfLines),
                            classifier("lines of " + dataType), FROM, classifier(contentResult.source),
                            stringValue(contentResult.path));
                },
                () -> {
                    ContentResult contentResult = dataTextContentImpl(fileOrResourcePath);
                    contentResult.parseResult = convertor.apply(contentResult.textContent);

                    return contentResult;
                }
        );

        ContentResult stepResult = step.execute(StepReportOptions.REPORT_ALL);
        return (R) stepResult.parseResult;
    }

    static ContentResult dataTextContentImpl(String fileOrResourcePath) {
        Path filePath = WebTauConfig.getCfg().getWorkingDir().resolve(fileOrResourcePath);

        boolean hasResource = ResourceUtils.hasResource(fileOrResourcePath);
        boolean hasFile = Files.exists(filePath);

        if (!hasResource && !hasFile) {
            throw new IllegalArgumentException("Can't find resource \"" + fileOrResourcePath + "\" or " +
                    "file \"" + filePath.toAbsolutePath() + "\"");
        }

        return hasResource ?
                new ContentResult("classpath resource", fileOrResourcePath,
                        ResourceUtils.textContent(fileOrResourcePath)) :
                new ContentResult("file", filePath.toAbsolutePath().toString(),
                        FileUtils.fileTextContent(filePath));
    }

    static class ContentResult {
        final String source;
        final String path;
        final String textContent;
        final int numberOfLines;
        Object parseResult;

        public ContentResult(String source, String path, String textContent) {
            this.source = source;
            this.path = path;
            this.textContent = textContent;
            this.numberOfLines = StringUtils.countMatches(textContent, '\n');
        }
    }
}
