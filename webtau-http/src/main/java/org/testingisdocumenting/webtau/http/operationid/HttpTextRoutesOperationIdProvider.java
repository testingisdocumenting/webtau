/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.http.operationid;

import org.testingisdocumenting.webtau.http.HttpHeader;
import org.testingisdocumenting.webtau.http.request.HttpRequestBody;

public class HttpTextRoutesOperationIdProvider implements HttpOperationIdProvider {
    @Override
    public String operationId(String requestMethod, String passedUrl, String fullUrl, HttpHeader requestHeader, HttpRequestBody requestBody) {
        HttpTextDefinedOperations textDefinedOperations = HttpTextFileOperations.getTextDefinedOperations();
        if (textDefinedOperations == null) {
            return "";
        }

        return textDefinedOperations.findOperationId(requestMethod, fullUrl);
    }

    @Override
    public boolean isEnabled() {
        return HttpTextFileOperations.getTextDefinedOperations() != null;
    }
}
