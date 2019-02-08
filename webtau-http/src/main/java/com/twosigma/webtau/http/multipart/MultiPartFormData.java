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

package com.twosigma.webtau.http.multipart;

import com.twosigma.webtau.http.request.HttpRequestBody;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MultiPartFormData implements HttpRequestBody {
    private final String boundary;
    private final List<MultiPartFormField> fields;

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
        return buildMultiPartRequest();
    }

    private byte[] buildMultiPartRequest() {
        MultiPartContentBuilder builder = new MultiPartContentBuilder();

        fields.forEach(field -> {
            builder.writeln("--" + boundary);
            field.writeRequest(builder);
            builder.newLine();
        });

        builder.writeln("--" + boundary + "--");

        return builder.toByteArray();
    }
}
