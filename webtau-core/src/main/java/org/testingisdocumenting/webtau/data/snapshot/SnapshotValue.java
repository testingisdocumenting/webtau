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

package org.testingisdocumenting.webtau.data.snapshot;

import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.testingisdocumenting.webtau.reporter.WebTauStep;

import java.util.function.Supplier;

/**
 * Snapshot value that maintains a state whether it was taken or not
 */
public class SnapshotValue {
    private Object value;
    private boolean isTaken;

    public static SnapshotValue empty() {
        return new SnapshotValue(null, false);
    }

    public void take(TokenizedMessage inProgressMessage, Supplier<TokenizedMessage> completionMessage, Supplier<Object> value) {
        WebTauStep.createAndExecuteStep(inProgressMessage, completionMessage, () -> {
            this.isTaken = true;
            this.value = value.get();
        });
    }

    private SnapshotValue(Object value, boolean isTaken) {
        this.value = value;
        this.isTaken = isTaken;
    }

    public void reset() {
        isTaken = false;
        value = null;
    }

    public Object required() {
       if (!isTaken) {
           throw new IllegalStateException("snapshot value was not taken, use takeSnapshot() prior to this call");
       }

       return value;
    }
}
