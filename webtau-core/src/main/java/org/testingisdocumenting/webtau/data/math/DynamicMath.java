package org.testingisdocumenting.webtau.data.math;

import org.testingisdocumenting.webtau.data.render.DataRenderers;
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
                        "\nleft: " + DataRenderers.render(left) + " " + TraceUtils.renderType(left) +
                        "\nright: " + DataRenderers.render(right) + " " + TraceUtils.renderType(right));
    }
}
