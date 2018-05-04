package com.twosigma.webtau.console;

import com.twosigma.webtau.utils.ServiceLoaderUtils;

import java.util.Set;

public class ConsoleOutputs {
    private static Set<ConsoleOutput> outputs = ServiceLoaderUtils.load(ConsoleOutput.class);

    public static void out(Object... styleOrValues) {
        outputs.forEach(o -> o.out(styleOrValues));
    }

    public static void err(Object... styleOrValues) {
        outputs.forEach(o -> o.err(styleOrValues));
    }

    public static void add(ConsoleOutput consoleOutput) {
        outputs.add(consoleOutput);
    }

    public static void remove(ConsoleOutput consoleOutput) {
        outputs.remove(consoleOutput);
    }
}
