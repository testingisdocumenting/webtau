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

package org.testingisdocumenting.webtau.server.route;

import org.testingisdocumenting.webtau.server.*;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

public class WebtauRouter implements WebtauServerOverride {
    private final WebtauServerOverrideList overrideList;

    public WebtauRouter(String id) {
        this.overrideList = new WebtauServerOverrideList(id);
    }

    @Override
    public boolean matchesUri(String method, String uri) {
        return overrideList.matchesUri(method, uri);
    }

    @Override
    public String overrideId() {
        return overrideList.overrideId();
    }

    @Override
    public WebtauServerResponse response(HttpServletRequest request) {
        return overrideList.response(request);
    }

    @Override
    public String toString() {
        return overrideList.toString();
    }

    public WebtauRouter get(String urlWithParams, Function<WebtauServerRequest, WebtauServerResponse> responseFunc) {
        return register("GET", urlWithParams, responseFunc);
    }

    public WebtauRouter post(String urlWithParams, Function<WebtauServerRequest, WebtauServerResponse> responseFunc) {
        return register("POST", urlWithParams, responseFunc);
    }

    public WebtauRouter put(String urlWithParams, Function<WebtauServerRequest, WebtauServerResponse> responseFunc) {
        return register("PUT", urlWithParams, responseFunc);
    }

    public WebtauRouter delete(String urlWithParams, Function<WebtauServerRequest, WebtauServerResponse> responseFunc) {
        return register("DELETE", urlWithParams, responseFunc);
    }

    public WebtauRouter patch(String urlWithParams, Function<WebtauServerRequest, WebtauServerResponse> responseFunc) {
        return register("PATCH", urlWithParams, responseFunc);
    }

    private WebtauRouter register(String method, String urlWithParams,
                                  Function<WebtauServerRequest, WebtauServerResponse> responseFunc) {
        overrideList.addOverride(new WebtauServerOverrideRouteFake(method, urlWithParams, responseFunc));
        return this;
    }
}
