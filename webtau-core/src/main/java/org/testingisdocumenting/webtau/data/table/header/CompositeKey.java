/*
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

package org.testingisdocumenting.webtau.data.table.header;

import org.testingisdocumenting.webtau.data.table.TableData;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Composite key to be used in structures like {@link TableData}.
 */
public class CompositeKey {
    private List<Object> values;

    public CompositeKey(Stream<Object> values) {
        this.values = values
                .map(CompositeKeyUnderlyingValueExtractors::extract)
                .collect(Collectors.toList());
    }

    public List<?> getValues() {
        return Collections.unmodifiableList(values);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;

        if (other.getClass() != CompositeKey.class)
            return false;

        List<Object> otherValues = ((CompositeKey) other).values;
        return values.equals(otherValues);
    }

    public int hashCode() {
        return values.stream().map(Object::hashCode).reduce(0, (l, r) -> l * 31 + r);
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
