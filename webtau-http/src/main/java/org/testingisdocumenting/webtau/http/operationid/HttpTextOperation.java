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

import org.testingisdocumenting.webtau.utils.UrlRouteRegexp;
import org.testingisdocumenting.webtau.utils.UrlUtils;

class HttpTextOperation {
    private final String operationId;
    private final UrlRouteRegexp urlRouteRegexp;

    public HttpTextOperation(String method, String route) {
        this.operationId = method + " " + route;
        this.urlRouteRegexp = UrlUtils.buildRouteRegexpAndGroupNames(route);
    }

    public String getOperationId() {
        return operationId;
    }

    public boolean matches(String url) {
        return urlRouteRegexp.matches(url);
    }
}
