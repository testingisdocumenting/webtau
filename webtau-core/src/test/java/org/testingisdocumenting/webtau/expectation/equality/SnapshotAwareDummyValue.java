/*
 * Copyright 2024 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.equality;

import org.testingisdocumenting.webtau.data.SnapshotValueAware;

import java.util.HashMap;
import java.util.Map;

public class SnapshotAwareDummyValue implements SnapshotValueAware {
    private Object snapshot;
    private final Map<String, Object> data = new HashMap<>();

    public void init() {
        data.put("id", "tid1");
        data.put("name", "name1");
    }

    public void doOperation() {
        data.put("name", "name1-changed");
    }

    @Override
    public void takeSnapshot() {
        snapshot = new HashMap<>(data);
    }

    @Override
    public Object snapshotValue() {
        return snapshot;
    }

    @Override
    public Object actualValue() {
        return data;
    }
}
