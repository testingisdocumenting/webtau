package org.testingisdocumenting.webtau.reporter;

import org.testingisdocumenting.webtau.console.ConsoleOutputs;
import org.testingisdocumenting.webtau.console.ansi.Color;

public class ConsoleTestListener implements TestListener {
    @Override
    public void beforeTestRun(WebTauTest test) {
        ConsoleOutputs.out(Color.BACKGROUND_YELLOW, Color.BLACK, "scenario", Color.RESET,
                Color.BLUE, " ", test.getScenario().trim(), " ",
                Color.PURPLE, "(" + test.getShortContainerId() + ")");
    }

    @Override
    public void afterTestRun(WebTauTest test) {
        if (test.isFailed()) {
            outAfter(Color.RED, "[x]", test);
        } else if (test.isErrored()) {
            outAfter(Color.RED, "[~]", test);
        } else if (test.isSkipped()) {
            outAfter(Color.YELLOW, "[o]", test);
        } else {
            outAfter(Color.GREEN, "[.]", test);
        }

        ConsoleOutputs.out();
    }

    private static void outAfter(Color color, String icon, WebTauTest test) {
        ConsoleOutputs.out(color, icon, ' ', Color.BLUE, test.getScenario().trim(),
                ' ', Color.PURPLE, '(' + test.getShortContainerId() + ')');
    }
}
