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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MultiPartFormData implements HttpRequestBody {
    private final String boundary;
    private List<MultiPartFormField> fields;

    public MultiPartFormData() {
        this.fields = new ArrayList<>();
        this.boundary = "webtau" + hashCode() + UUID.randomUUID().toString();
    }

    public MultiPartFormData(Map<String, Object> fields) {
        this();

        fields.forEach((fieldName, content) -> {
            if (content instanceof MultiPartFile) {
                MultiPartFile file = (MultiPartFile) content;
                addField(MultiPartFormField.binaryFormField(fieldName, file.getContent(), file.getName()));
            } else if (content instanceof byte[]) {
                addField(MultiPartFormField.binaryFormField(fieldName, (byte[]) content, null));
            } else if (content instanceof String) {
                addField(MultiPartFormField.textFormField(fieldName, (String) content, null));
            } else if (content instanceof Path) {
                addField(MultiPartFormField.fileFormField(fieldName, (Path) content));
            } else {
                throw new UnsupportedOperationException("form field of type <" + content.getClass() + "> is not supported");
            }
        });
    }

    public void addField(MultiPartFormField field) {
        fields.add(field);
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
        try {
            return buildMultiPartRequest();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] buildMultiPartRequest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        fields.forEach(field -> {
            writeBoundary(outputStream);
            field.writeRequest(outputStream);
            write(outputStream, "\r\n");
        });

        write(outputStream,"--" + boundary + "--\r\n");
        outputStream.flush();
        outputStream.close();

        return outputStream.toByteArray();
    }

    private void writeBoundary(OutputStream outputStream) {
        write(outputStream, "--" + boundary + "\r\n");
    }

    private void write(OutputStream outputStream, String text) {
        try {
            outputStream.write(text.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
