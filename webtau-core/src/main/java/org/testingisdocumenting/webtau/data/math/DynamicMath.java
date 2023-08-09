/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.data.math;

import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;
import org.testingisdocumenting.webtau.utils.TraceUtils;

import java.util.List;

public class DynamicMath {
    private static final List<SimpleMathHandler> simpleMathHandlers = discoverSimpleMathHandlers();

    private DynamicMath() {
    }

    public static Object add(Object left, Object right) {
        return findAddSubtractMathHandler(left, right).add(left, right);
    }

    public static Object subtract(Object left, Object right) {
        return findAddSubtractMathHandler(left, right).subtract(left, right);
    }

    private static SimpleMathHandler findAddSubtractMathHandler(Object left, Object right) {
        return simpleMathHandlers.stream().
                filter(h -> h.handleAddSubtract(left, right)).findFirst().
                orElseThrow(() -> noAddSubtractHandlerFound(left, right));
    }

    private static List<SimpleMathHandler> discoverSimpleMathHandlers() {
        return ServiceLoaderUtils.load(SimpleMathHandler.class);
    }

    private static RuntimeException noAddSubtractHandlerFound(Object left, Object right) {
        return new RuntimeException(
                "no add/subtract handler found for" +
                        "\nleft: " + PrettyPrinter.renderAsTextWithoutColors(left) + " " + TraceUtils.renderType(left) +
                        "\nright: " + PrettyPrinter.renderAsTextWithoutColors(right) + " " + TraceUtils.renderType(right));
    }
}
