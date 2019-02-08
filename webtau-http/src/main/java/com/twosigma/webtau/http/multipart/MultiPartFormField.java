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

import com.twosigma.webtau.utils.FileUtils;

import java.nio.file.Path;

public class MultiPartFormField {
    private final byte[] content;
    private final String contentType;
    private final String fieldName;
    private final String fileName;

    public static MultiPartFormField binaryFormField(String fieldName, byte[] content, String fileName) {
        return new MultiPartFormField(fieldName, "application/octet-stream", content, fileName);
    }

    public static MultiPartFormField textFormField(String fieldName, String content, String fileName) {
        return new MultiPartFormField(fieldName, null, content.getBytes(), fileName);
    }

    public static MultiPartFormField fileFormField(String fieldName, Path file) {
        return fileFormField(fieldName, file, file.getFileName().toString());
    }

    public static MultiPartFormField fileFormField(String fieldName, Path file, String fileName) {
        return binaryFormField(fieldName, FileUtils.fileBinaryContent(file), fileName);
    }

    private MultiPartFormField(String fieldName, String contentType, byte[] content, String fileName) {
        this.content = content;
        this.contentType = contentType;
        this.fieldName = fieldName;
        this.fileName = fileName;
    }

    void writeRequest(MultiPartContentBuilder builder) {
        builder.writeHeader("Content-Disposition", "form-data");
        builder.write("; ");
        builder.writeAttr("name", fieldName);
        builder.write("; ");

        if (fileName != null) {
            builder.writeAttr("filename", fileName);
            builder.newLine();
        } else {
            builder.newLine();
        }

        if (contentType != null) {
            builder.writeHeader("Content-Type", contentType);
            builder.newLine();
        }

        builder.newLine();
        builder.write(content);
    }
}
