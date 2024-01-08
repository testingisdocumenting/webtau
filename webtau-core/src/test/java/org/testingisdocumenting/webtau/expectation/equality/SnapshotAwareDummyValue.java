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

import org.testingisdocumenting.webtau.data.snapshot.SnapshotValueAware;

import java.util.*;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class SnapshotAwareDummyValue implements SnapshotValueAware {
    private Object snapshot;
    private List<Map<String, Object>> data;
    private boolean incrementValueIdx;
    private int currentValueIdx; // for wait testing

    public SnapshotAwareDummyValue() {
        data = list(
                map("id", "tid1", "name", "name1"),
                map("id", "tid1", "name", "name1-1"),
                map("id", "tid1", "name", "name1-2")
        );
    }

    public void doOperation() {
        data.set(0, map("id", "tid1", "name", "name1-changed"));
    }

    public void makeEmpty() {
        data = new ArrayList<>();
        data.add(new HashMap<>());
    }

    public void enableAutoIncrement() {
       incrementValueIdx = true;
    }

    @Override
    public void takeSnapshot() {
        snapshot = new HashMap<>(data.get(currentValueIdx));
    }

    @Override
    public Object snapshotValue() {
        return snapshot;
    }

    @Override
    public Object currentValue() {
        return data.get(incrementValueIdx ? currentValueIdx++ : currentValueIdx);
    }
}
