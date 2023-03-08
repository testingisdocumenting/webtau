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

package org.testingisdocumenting.webtau.fs;

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.ActualPathAndDescriptionAware;
import org.testingisdocumenting.webtau.expectation.ActualValueExpectations;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.utils.FileUtils;
import org.testingisdocumenting.webtau.utils.RegexpUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import static org.testingisdocumenting.webtau.WebTauCore.tokenizedMessage;

public class FileTextContent implements ActualValueExpectations, ActualPathAndDescriptionAware {
    private final ValuePath actualPath;
    private final Path path;

    public FileTextContent(Path path) {
        this.actualPath = new ValuePath("file <" + path.getFileName().toString() + ">");
        this.path = path;
    }

    /**
     * reads data from a file, consequent calls may return a different data
     * @return current file text content
     */
    public String getData() {
        if (!Files.exists(path)) {
            return null;
        }

        return FileUtils.fileTextContent(path);
    }

    /**
     * reads data from a file, consequent calls may return a different data.
     * Wraps reading in a reportable step. If you need to read data in a loop to wait for something use {@link #getData}
     * @return current file text content
     */
    public String getDataWithReportedStep() {
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage().action("reading text").from().url(path),
                (r) -> tokenizedMessage().action("read text").from().url(path).of()
                        .classifier("size").number(r.toString().length()),
                this::getData);

        return step.execute(StepReportOptions.REPORT_ALL);
    }

    @Override
    public ValuePath actualPath() {
        return actualPath;
    }

    public String extractByRegexp(String regexp) {
        return extractByRegexp(Pattern.compile(regexp));
    }

    public String extractByRegexp(Pattern regexp) {
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage().action("extracting text").classifier("by regexp").string(regexp)
                        .from().url(path),
                (r) -> tokenizedMessage().action("extracted text").classifier("by regexp").string(regexp)
                        .from().url(path).colon().string(r),
                () -> extractByRegexpStepImpl(regexp));

        return step.execute(StepReportOptions.REPORT_ALL);
    }

    @Override
    public StepReportOptions shouldReportOption() {
        return StepReportOptions.REPORT_ALL;
    }

    @Override
    public String toString() {
        return getData();
    }

    private String extractByRegexpStepImpl(Pattern regexp) {
        String extracted = RegexpUtils.extractByRegexp(getData(), regexp);
        if (extracted == null) {
            throw new RuntimeException("can't find content to extract using regexp <" + regexp + "> from: " + path);
        }

        return extracted;
    }
}
