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

package com.twosigma.webtau.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StreamGobbler implements Runnable {
    private final String id;
    private final InputStream stream;
    private final List<String> lines;

    private IOException exception;

    public StreamGobbler(String id, InputStream stream) {
        this.id = id;
        this.stream = stream;
        this.lines = new ArrayList<>();
    }

    public List<String> getLines() {
        return lines;
    }

    @Override
    public void run() {
        InputStreamReader inputStreamReader = new InputStreamReader(stream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        try {
            consume(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace(); // TODO remove
            exception = e;
        }
    }

    private void consume(BufferedReader bufferedReader) throws IOException {
        for (;;) {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }

            lines.add(line);
        }
    }
}
