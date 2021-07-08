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

package org.testingisdocumenting.webtau.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public interface WebtauServerOverride {
    boolean matchesUri(String method, String uri);

    String overrideId();

    byte[] responseBody(HttpServletRequest request) throws IOException, ServletException;
    String responseType(HttpServletRequest request);

    default Map<String, String> responseHeader(HttpServletRequest request) {
        return Collections.emptyMap();
    }

    default int responseStatusCode() {
        return 200;
    }
}
