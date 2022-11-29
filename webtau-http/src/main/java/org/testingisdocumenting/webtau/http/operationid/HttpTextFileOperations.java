/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.http.operationid;

import org.testingisdocumenting.webtau.cfg.WebTauConfig;
import org.testingisdocumenting.webtau.cfg.WebTauConfigHandler;
import org.testingisdocumenting.webtau.http.config.HttpConfig;
import org.testingisdocumenting.webtau.reporter.MessageToken;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.utils.FileUtils;
import org.testingisdocumenting.webtau.utils.ResourceUtils;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.*;

public class HttpTextFileOperations implements WebTauConfigHandler {
    private static HttpTextDefinedOperations textDefinedOperations;

    // reset in case of multiple runs within the same JVM
    @Override
    public void onAfterCreate(WebTauConfig cfg) {
        textDefinedOperations = null;
    }

    public synchronized static HttpTextDefinedOperations getTextDefinedOperations() {
        if (textDefinedOperations != null) {
            return textDefinedOperations;
        }

        String path = HttpConfig.getTextOperationsPath();
        if (path.isEmpty()) {
            return null;
        }

        Path fullPath = WebTauConfig.getCfg().fullPath(path);
        if (Files.exists(fullPath)) {
            textDefinedOperations = buildTextDefinedOperations(fullPath, null);
        } else if (ResourceUtils.hasResource(path)) {
            textDefinedOperations = buildTextDefinedOperations(null, path);
        } else {
            throw new RuntimeException("Can't find neither http routes file <" + fullPath + "> nor classpath resource <" + path + ">");
        }

        return textDefinedOperations;
    }

    private static HttpTextDefinedOperations buildTextDefinedOperations(Path filePath, String resourcePath) {
        String pathToUse = filePath != null ? filePath.toString() : resourcePath;
        String content = filePath != null ? FileUtils.fileTextContent(filePath) : ResourceUtils.textContent(resourcePath);
        String source = filePath != null ? "file" : "class path resource";

        MessageToken spec = classifier("HTTP routes definition");
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage(action("reading"), spec,
                        FROM, classifier(source), urlValue(pathToUse)),
                () -> tokenizedMessage(action("read"), spec,
                        FROM, classifier(source), urlValue(pathToUse)),
                () -> new HttpTextDefinedOperations(content)
        );

        return step.execute(StepReportOptions.REPORT_ALL);
    }
}
