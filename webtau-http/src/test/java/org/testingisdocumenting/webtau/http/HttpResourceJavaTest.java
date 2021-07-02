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

package org.testingisdocumenting.webtau.http;

import org.junit.Test;

import static org.testingisdocumenting.webtau.Matchers.*;
import static org.testingisdocumenting.webtau.http.Http.*;

public class HttpResourceJavaTest extends HttpTestBase {
    @Test
    public void shouldReadResource() {
        HttpResource resource = http.resource("/end-point-simple-object");

        resource.get("id").should(equal("id1"));
        resource.get("k1").should(equal("v1"));
        resource.reRead().get("k2").should(equal("v2"));
    }

    @Test
    public void shouldWaitOnResourceValue() {
        HttpResource resource = http.resource("/end-point-simple-object");

        resource.get("id").waitTo(equal("id2"));
        resource.get("k2").should(equal("v2"));
    }

    // TODO implicit statusCode check may not make sense here
}