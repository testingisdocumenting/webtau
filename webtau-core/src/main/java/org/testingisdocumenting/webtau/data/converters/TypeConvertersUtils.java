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

package org.testingisdocumenting.webtau.data.converters;

import org.testingisdocumenting.webtau.utils.TraceUtils;

import java.util.Objects;
import java.util.stream.Stream;

class TypeConvertersUtils {
    public static <E> E convert(Stream<? extends ToTypeConverter<E>> converters, String typeName, Object v) {
        if (v == null) {
            return null;
        }

        return converters
                .map(h -> h.convert(v))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    static <E> E convertAndThrow(Stream<? extends ToTypeConverter<E>> converters, String typeName, Object v) {
        E result = convert(converters, typeName, v);
        if (result == null) {
            throw new IllegalArgumentException("can't find a " + typeName +
                    " converter for: " + TraceUtils.renderValueAndType(v));
        }

        return result;
    }
}
