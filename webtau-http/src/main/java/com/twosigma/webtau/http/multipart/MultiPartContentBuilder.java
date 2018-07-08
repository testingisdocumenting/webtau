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

package com.twosigma.webtau.http.multipart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

class MultiPartContentBuilder {
    private static final String LINE_END = "\r\n";

    private final ByteArrayOutputStream outputStream;

    MultiPartContentBuilder() {
        outputStream = new ByteArrayOutputStream();
    }

    void write(String text) {
        try {
            outputStream.write(text.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void writeln(String text) {
        write(text);
        newLine();
    }

    void writeHeader(String name, String value) {
        write(name + ": " + value);
    }

    void writeAttr(String name, String value) {
        write(name + "=" + "\"" + value + "\"");
    }

    void newLine() {
        write(LINE_END);
    }

    void write(byte[] content) {
        try {
            outputStream.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    byte[] toByteArray() {
        return outputStream.toByteArray();
    }
}
