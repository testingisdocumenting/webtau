package com.twosigma.webtau.expectation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.twosigma.webtau.expectation.ExpectationHandler.Flow;
import com.twosigma.webtau.utils.ServiceUtils;

public class ExpectationHandlers {
    private static List<ExpectationHandler> globalHandlers = ServiceUtils.discover(ExpectationHandler.class);
    private static ThreadLocal<List<ExpectationHandler>> localHandlers = ThreadLocal.withInitial(ArrayList::new);

    public static void add(ExpectationHandler handler) {
        globalHandlers.add(handler);
    }

    public static void remove(ExpectationHandler handler) {
        globalHandlers.remove(handler);
    }

    public static void addLocal(ExpectationHandler handler) {
        localHandlers.get().add(handler);
    }

    public static void removeLocal(ExpectationHandler handler) {
        localHandlers.get().remove(handler);
    }

    public static Flow onValueMismatch(ActualPath actualPath, Object actualValue, String message) {
        return Stream.concat(localHandlers.get().stream(), globalHandlers.stream())
                .map(h -> h.onValueMismatch(actualPath, actualValue, message))
                .filter(flow -> flow == Flow.Terminate)
                .findFirst().orElse(Flow.PassToNext);
    }
}
