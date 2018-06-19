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

import com.twosigma.webtau.http.HttpRequestBody;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.UUID;

public class MultiPartFileRequestBody implements HttpRequestBody {
    private final String boundary;

    private final String fieldName;
    private final String fileName;
    private final byte[] fileContent;
    private final byte[] request;

    public MultiPartFileRequestBody(String fieldName, String fileName, byte[] fileContent) {
        this.boundary = hashCode() + UUID.randomUUID().toString();

        this.fieldName = fieldName;
        this.fileName = fileName;
        this.fileContent = fileContent;

        try {
            this.request = buildMultiPartRequest();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isBinary() {
        return true;
    }

    @Override
    public String type() {
        return "multipart/form-data; boundary=" + boundary;
    }

    @Override
    public String asString() {
        return null;
    }

    @Override
    public byte[] asBytes() {
        return request;
    }

    private byte[] buildMultiPartRequest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));

        writer.write("--" + boundary + "\n");
        writer.write("Content-Disposition: form-data; name=\"" +
                fieldName + "\"; filename=\"" +
                fileName +"\"\n");
        writer.write("Content-Type: application/octet-stream\n");
        writer.write("Content-Transfer-Encoding: binary\n\n");
        writer.flush();

        outputStream.write(fileContent);
        outputStream.flush();

        writer.write("\n");
        writer.write("--" + boundary + "--");
        writer.flush();

        return outputStream.toByteArray();
    }
}
