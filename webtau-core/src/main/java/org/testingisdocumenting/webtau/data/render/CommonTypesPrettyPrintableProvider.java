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

package org.testingisdocumenting.webtau.data.render;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

public class CommonTypesPrettyPrintableProvider implements PrettyPrintableProvider {
    @Override
    public Optional<PrettyPrintable> prettyPrintableFor(Object o) {
        if (o == null) {
            return Optional.empty();
        }

        if (o.getClass().isArray()) {
            return Optional.of(new ArrayPrettyPrintable(o));
        } if (o instanceof Path) {
            return Optional.of(new PathPrettyPrintable((Path) o));
        } if (o instanceof Iterable) {
            return Optional.of(new IterablePrettyPrintable((Iterable<?>) o));
        } else if (o instanceof Map) {
            return Optional.of(new MapPrettyPrintable((Map<?, ?>) o));
        }

        return Optional.empty();
    }
}
