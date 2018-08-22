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

package com.twosigma.webtau.http;

import com.twosigma.webtau.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private String textContent;
    private byte[] binaryContent;
    private String contentType;

    private int statusCode;
    private HttpHeader header;

    public HttpResponse() {
        header = new HttpHeader();
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String content) {
        this.textContent = content;
    }

    public boolean nullOrEmptyTextContent() {
        return StringUtils.nullOrEmpty(textContent);
    }

    public byte[] getBinaryContent() {
        return binaryContent;
    }

    public void setBinaryContent(byte[] binaryContent) {
        this.binaryContent = binaryContent;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getContentType() {
        return contentType;
    }

    public boolean hasContent() {
        return binaryContent != null || (textContent != null && !textContent.isEmpty());
    }

    public boolean isJson() {
        return contentType.contains("/json");
    }

    public boolean isXml() {
        return contentType.contains("/xml");
    }

    public boolean isText() {
        return contentType.startsWith("text/");
    }

    public boolean isPdf() {
        return contentType.contains("/pdf");
    }

    public boolean isBinary() {
        return !isJson() && !isXml() && !isText() && !contentType.isEmpty();
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public HttpHeader getHeader() {
        return header;
    }

    public void addHeader(String key, String value) {
        header.add(key, value);
    }
}
