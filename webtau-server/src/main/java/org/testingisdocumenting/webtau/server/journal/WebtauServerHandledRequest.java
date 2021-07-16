/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.server.journal;

public class WebtauServerHandledRequest {
    private final String method;
    private final String url;
    private final String contentType;
    private final long startTime;
    private final long elapsedTime;

    public WebtauServerHandledRequest(String method, String url, String contentType, long startTime, long elapsedTime) {
        this.method = method;
        this.url = url;
        this.contentType = contentType;
        this.startTime = startTime;
        this.elapsedTime = elapsedTime;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getContentType() {
        return contentType;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }
}
