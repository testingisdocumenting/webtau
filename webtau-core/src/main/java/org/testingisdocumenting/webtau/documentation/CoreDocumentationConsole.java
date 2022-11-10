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

package org.testingisdocumenting.webtau.documentation;

import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.*;
import static org.testingisdocumenting.webtau.utils.StringUtils.*;

public class CoreDocumentationConsole {
    /**
     * capture the output of a provided code into the file with provided artifact name.
     * only one capture code will run at a time in multithreaded environment
     *
     * @param textFileNameWithoutExtension artifact name
     * @param codeToProduceOutput          code to run and capture output
     */
    public void capture(String textFileNameWithoutExtension, Runnable codeToProduceOutput) {
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage(action("capturing"), classifier("console output"),
                        action("documentation artifact"), id(textFileNameWithoutExtension)),
                (path) -> tokenizedMessage(action("captured"), classifier("console output"),
                        action("documentation artifact"), id(textFileNameWithoutExtension), COLON, urlValue(((Path)path).toAbsolutePath())),
                () -> captureStep(textFileNameWithoutExtension, codeToProduceOutput));

        step.execute(StepReportOptions.REPORT_ALL);
    }

    /**
     * capture the output of a provided code into the file with provided artifact name without using WebTau step.
     * only one capture code will run at a time in multithreaded environment
     *
     * @param textFileNameWithoutExtension artifact name
     * @param codeToProduceOutput          code to run and capture output
     */
    public void captureNoStep(String textFileNameWithoutExtension, Runnable codeToProduceOutput) {
        captureStep(textFileNameWithoutExtension, codeToProduceOutput);
    }

    synchronized private Path captureStep(String textFileNameWithoutExtension, Runnable codeToProduceOutput) {
        PrintStream previous = System.out;
        ByteArrayOutputStream captureStream = new ByteArrayOutputStream();

        Thread captureMainThread = Thread.currentThread();

        try {
            System.setOut(new PrintStream(new CombinedOutputStream(captureMainThread, previous, captureStream)));

            codeToProduceOutput.run();
            return DocumentationArtifacts.captureText(textFileNameWithoutExtension,
                    stripIndentation(captureStream.toString()));
        } finally {
            System.setOut(previous);
        }
    }

    private static class CombinedOutputStream extends OutputStream {
        private final Thread captureThread;
        private final OutputStream original;
        private final OutputStream capture;

        public CombinedOutputStream(Thread captureThread, OutputStream original, OutputStream capture) {
            this.captureThread = captureThread;
            this.original = original;
            this.capture = capture;
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            original.write(b, off, len);
            if (isThreadToCapture()) {
                capture.write(b, off, len);
            }
        }

        @Override
        public void write(int b) throws IOException {
            original.write(b);
            if (isThreadToCapture()) {
                capture.write(b);
            }
        }

        @Override
        public void flush() throws IOException {
            original.flush();
            if (isThreadToCapture()) {
                capture.flush();
            }
        }

        @Override
        public void close() throws IOException {
            original.close();
            if (isThreadToCapture()) {
                capture.close();
            }
        }

        private boolean isThreadToCapture() {
            return captureThread == Thread.currentThread();
        }
    }
}
