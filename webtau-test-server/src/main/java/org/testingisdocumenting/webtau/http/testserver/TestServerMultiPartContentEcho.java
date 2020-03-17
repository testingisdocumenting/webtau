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

package com.twosigma.webtau.http.testserver;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class TestServerMultiPartContentEcho implements TestServerResponse {
    private final int statusCode;
    private int partIdx;

    public TestServerMultiPartContentEcho(int statusCode, int partIdx) {
        this.statusCode = statusCode;
        this.partIdx = partIdx;
    }

    @Override
    public byte[] responseBody(HttpServletRequest request) throws IOException, ServletException {
        Collection<Part> parts = request.getParts();

        Part part = new ArrayList<>(parts).get(partIdx);
        return IOUtils.toByteArray(part.getInputStream());
    }

    @Override
    public String responseType(HttpServletRequest request) {
        return request.getContentType();
    }

    @Override
    public int responseStatusCode() {
        return statusCode;
    }
}
