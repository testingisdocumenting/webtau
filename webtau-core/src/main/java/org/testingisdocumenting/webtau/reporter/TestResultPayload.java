/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.reporter;

import java.util.Collections;
import java.util.Map;

public class TestResultPayload {
    private final String payloadName;
    private final Object payload;

    public TestResultPayload(String payloadName, Object payload) {
        this.payloadName = payloadName;
        this.payload = payload;
    }

    public String getPayloadName() {
        return payloadName;
    }

    public Object getPayload() {
        return payload;
    }

    public Map<String, ?> toMap() {
        return Collections.singletonMap(payloadName, payload);
    }
}