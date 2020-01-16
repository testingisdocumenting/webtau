package com.twosigma.webtau.reporter;

import com.twosigma.webtau.utils.ServiceLoaderUtils;

import java.util.List;

public class TestListeners {
    private static List<TestListener> listeners = ServiceLoaderUtils.load(TestListener.class);

    private TestListeners() {
    }

    public static void beforeTestRun(WebTauTest test) {
        listeners.forEach(listener -> listener.beforeTestRun(test));
    }

    public static void afterTestRun(WebTauTest test) {
        listeners.forEach(listener -> listener.afterTestRun(test));
    }

    public static void beforeFirstTest() {
        listeners.forEach(TestListener::beforeFirstTest);
    }

    public static void afterAllTests(WebTauReport report) {
        listeners.forEach(listeners -> listeners.afterAllTests(report));
    }

    public static void beforeFirstTestStatement(WebTauTest test) {
        listeners.forEach(listeners -> listeners.beforeFirstTestStatement(test));
    }

    public static void afterLastTestStatement(WebTauTest test) {
        listeners.forEach(listeners -> listeners.afterLastTestStatement(test));
    }

    public static void add(TestListener listener) {
        listeners.add(listener);
    }

    public static void remove(TestListener listener) {
        listeners.remove(listener);
    }
}
