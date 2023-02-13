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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.testingisdocumenting.webtau.WebTauCore.tokenizedMessage;

class DataContentUtils {
    private DataContentUtils() {
    }

    @SuppressWarnings("unchecked")
    static <R> R readAndConvertTextContentFromDataPathAsStep(String dataType, DataPath dataPath, Function<String, R> convertor) {
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage().action("reading").classifier(dataType).from().classifier("file or resource").url(dataPath.getGivenPathAsString()),
                (result) -> {
                    ExternalContentResult contentResult = (ExternalContentResult) result;
                    return tokenizedMessage().action("read").number(contentResult.numberOfLines)
                            .classifier(lineOrLinesLabel(contentResult.numberOfLines) + " of " + dataType).from().classifier(contentResult.source).url(contentResult.path);
                },
                () -> {
                    ExternalContentResult contentResult = dataTextContentImpl(dataPath);
                    contentResult.parseResult = convertor.apply(contentResult.textContent);

                    return contentResult;
                }
        );

        ExternalContentResult stepResult = step.execute(StepReportOptions.REPORT_ALL);
        return (R) stepResult.parseResult;
    }

    @SuppressWarnings("unchecked")
    static <R> R convertTextContent(String dataType, String content, Function<String, R> convertor) {
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage().action("parsing").classifier(dataType).from().classifier("string"),
                (result) -> {
                    ContentResult contentResult = (ContentResult) result;
                    return tokenizedMessage().action("parsed").number(contentResult.numberOfLines)
                            .classifier(lineOrLinesLabel(contentResult.numberOfLines) + " of " + dataType);
                },
                () -> {
                    ContentResult contentResult = new ContentResult(content);
                    contentResult.parseResult = convertor.apply(content);

                    return contentResult;
                }
        );

        ContentResult stepResult = step.execute(StepReportOptions.REPORT_ALL);
        return (R) stepResult.parseResult;
    }

    static Path writeTextContentAsStep(String dataType, Path path, Supplier<String> convertor) {
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage().action("writing").classifier(dataType).to().classifier("file").url(path),
                (result) -> {
                    ExternalContentResult contentResult = (ExternalContentResult) result;

                    return tokenizedMessage().action("wrote").number(contentResult.numberOfLines)
                            .classifier(lineOrLinesLabel(contentResult.numberOfLines)).to()
                            .classifier(dataType).url(contentResult.path);
                },
                () -> {
                    Path fullPath = WebTauConfig.getCfg().fullPath(path);

                    String content = convertor.get();
                    FileUtils.writeTextContent(fullPath, content);

                    return new ExternalContentResult("file", fullPath.toString(), content);
                }
        );

        ExternalContentResult stepResult = step.execute(StepReportOptions.REPORT_ALL);
        return Paths.get(stepResult.path);
    }

    static ExternalContentResult dataTextContentImpl(DataPath path) {
        if (!path.isResource() && !path.isFile()) {
            if (path.isResourceSpecified()) {
                throw new IllegalArgumentException("Can't find resource \"" + path.getFileOrResourcePath() + "\" or " +
                        "file \"" + path.getFullFilePath() + "\"");
            } else {
                throw new IllegalArgumentException("Can't find file \"" + path.getFullFilePath() + "\"");
            }
        }

        return path.isResource() ?
                new ExternalContentResult("classpath resource", path.getFileOrResourcePath(),
                        ResourceUtils.textContent(path.getFileOrResourcePath())) :
                new ExternalContentResult("file", path.getFullFilePath().toString(),
                        FileUtils.fileTextContent(path.getFullFilePath()));
    }

    static String lineOrLinesLabel(int count) {
        return count == 1 ? "line" : "lines";
    }

    static class ContentResult {
        final int numberOfLines;
        Object parseResult;

        public ContentResult(String textContent) {
            this.numberOfLines = StringUtils.countMatches(textContent, '\n') + 1;
        }
    }

    static class ExternalContentResult {
        final String source;
        final String path;
        final String textContent;
        final int numberOfLines;
        Object parseResult;

        public ExternalContentResult(String source, String path, String textContent) {
            this.source = source;
            this.path = path;
            this.textContent = textContent;
            this.numberOfLines = StringUtils.countMatches(textContent, '\n') + 1;
        }
    }
}
