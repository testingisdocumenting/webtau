/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.http.resource;

import org.testingisdocumenting.webtau.data.datanode.DataNodeId;

import java.util.Collections;

public class HttpResource {
    public final HttpLazyResponseValue body;

    public HttpResource(String definitionGet) {
        this.body = new HttpLazyResponseValue(
                new HttpResourceDefinition(definitionGet, Collections.emptyMap()),
                new DataNodeId());
    }

    /**
     * declares response at a specific path, e.g. details.price[0]
     * @param path path to a value
     * @return lazy response value
     */
    public HttpLazyResponseValue get(String path) {
        return body.get(path);
    }

    /**
     * declares response at a specific index
     * @param idx index in the response
     * @return lazy response value
     */
    public HttpLazyResponseValue get(int idx) {
        return body.get(idx);
    }

    /**
     * alias to {@link #get(String)} for Groovy DSL
     * @param idx index in the response
     * @return lazy response value
     */
    public HttpLazyResponseValue getAt(int idx) {
        return body.get(idx);
    }
}
