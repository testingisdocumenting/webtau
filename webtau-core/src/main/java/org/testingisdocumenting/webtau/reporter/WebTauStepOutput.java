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

package org.testingisdocumenting.webtau.reporter;

import org.testingisdocumenting.webtau.data.render.PrettyPrintable;

import java.util.Collections;
import java.util.Map;

public interface WebTauStepOutput extends PrettyPrintable {
    WebTauStepOutput EMPTY = new Empty();

    Map<String, ?> toMap();

    default boolean isEmpty() {
        return false;
    }

    class Empty implements WebTauStepOutput {
        @Override
        public Map<String, ?> toMap() {
            return Collections.emptyMap();
        }

        @Override
        public void prettyPrint() {
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }
}
