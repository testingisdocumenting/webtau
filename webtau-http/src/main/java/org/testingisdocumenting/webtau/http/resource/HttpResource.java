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

import org.testingisdocumenting.webtau.http.datanode.DataNodeId;

import java.util.Collections;

public class HttpResource {
    private final HttpResourceDefinition definitionGet;

    public HttpResource(String definitionGet) {
        this.definitionGet = new HttpResourceDefinition(definitionGet, Collections.emptyMap());
        this.body = new HttpLazyDataNode(this.definitionGet, new DataNodeId(""));
    }

    public final HttpLazyDataNode body;
}
