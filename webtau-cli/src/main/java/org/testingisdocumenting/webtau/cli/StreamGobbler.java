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

package org.testingisdocumenting.webtau.cli;

import org.testingisdocumenting.webtau.console.ConsoleOutputs;
import org.testingisdocumenting.webtau.reporter.TestStep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg;

public class StreamGobbler implements Runnable {
    private final InputStream stream;
    private final List<String> lines;
    private final boolean renderOutput;

    private IOException exception;

    public StreamGobbler(InputStream stream) {
        this.stream = stream;
        this.lines = new ArrayList<>();
        this.renderOutput = shouldRenderOutput();
    }

    public List<String> getLines() {
        return lines;
    }

    public IOException getException() {
        return exception;
    }

    public void close() {
        try {
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        InputStreamReader inputStreamReader = new InputStreamReader(stream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        try {
            consume(bufferedReader);
        } catch (IOException e) {
            exception = e;
        }
    }

    private void consume(BufferedReader bufferedReader) throws IOException {
        for (;;) {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }

            if (renderOutput) {
                ConsoleOutputs.out(line);
            }

            lines.add(line);
        }
    }

    private boolean shouldRenderOutput() {
        TestStep<?, ?> currentStep = TestStep.getCurrentStep();
        int numberOfParents = currentStep == null ? 0 : currentStep.getNumberOfParents();

        return getCfg().getVerbosityLevel() > numberOfParents + 1;
    }
}
