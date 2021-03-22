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

package org.testingisdocumenting.webtau.utils;

import java.util.function.Function;
import java.util.function.Supplier;

public class FunctionUtils {
    public static Supplier<Object> toSupplier(Runnable r) {
        return () -> {
            r.run();
            return null;
        };
    }

    public static <P, R> Function<P, R> toFunction(Runnable r) {
        return (ignored) -> {
            r.run();
            return null;
        };
    }

    public static  <P, R> Function<P, R> toFunction(Supplier<R> s) {
        return (ignored) -> s.get();
    }
}
