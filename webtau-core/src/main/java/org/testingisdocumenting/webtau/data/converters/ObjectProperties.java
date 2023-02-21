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

package org.testingisdocumenting.webtau.data.converters;

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.data.render.PrettyPrintable;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;

import java.util.*;
import java.util.stream.Collectors;

public class ObjectProperties implements PrettyPrintable {
    private final Map<String, ?> topLevelProperties;
    private final Map<String, ?> unwrappedProperties;

    public ObjectProperties(Object object) {
        topLevelProperties = ToMapConverters.convert(object);
        unwrappedProperties = unwrapMapProperties(topLevelProperties);
    }

    public Map<String, ?> getTopLevelProperties() {
        return topLevelProperties;
    }

    public Map<String, ?> getUnwrappedProperties() {
        return unwrappedProperties;
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        printer.printObject(unwrappedProperties);
    }

    @Override
    public void prettyPrint(PrettyPrinter printer, ValuePath rootPath) {
        printer.printObject(rootPath, unwrappedProperties);
    }

    private Map<String, ?> unwrapMapProperties(Map<?, ?> properties) {
        Map<String, Object> result = new LinkedHashMap<>();
        properties.forEach((k, v) -> result.put(Objects.toString(k), unwrappedProperties(v)));

        return result;
    }

    private List<Object> unwrapCollectionProperties(Collection<?> properties) {
        return properties.stream()
                .map(this::unwrappedProperties)
                .collect(Collectors.toList());
    }

    private Object unwrappedProperties(Object v) {
        if (v instanceof Collection) {
            return unwrapCollectionProperties((Collection<?>)v);
        } else if (v instanceof Map) {
            return unwrapMapProperties((Map<?, ?>) v);
        } else if (v instanceof Number) {
            return v;
        } else if (PrettyPrinter.isPrettyPrintable(v)) {
            return v;
        } else {
            return unwrapMapProperties(ToMapConverters.convert(v));
        }
    }
}