package com.twosigma.webtau.data.math;

import com.twosigma.webtau.data.render.DataRenderers;
import com.twosigma.webtau.utils.ServiceUtils;
import com.twosigma.webtau.utils.TraceUtils;

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
        return ServiceUtils.discover(SimpleMathHandler.class);
    }

    private static RuntimeException noAddSubtractHandlerFound(Object left, Object right) {
        return new RuntimeException(
                "no add/subtract handler found for" +
                        "\nleft: " + DataRenderers.render(left) + " " + TraceUtils.renderType(left) +
                        "\nright: " + DataRenderers.render(right) + " " + TraceUtils.renderType(right));
    }
}
