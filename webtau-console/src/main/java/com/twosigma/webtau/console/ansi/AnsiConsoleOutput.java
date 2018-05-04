package com.twosigma.webtau.console.ansi;

import com.twosigma.webtau.console.ConsoleOutput;

public class AnsiConsoleOutput implements ConsoleOutput {
    @Override
    public void out(Object... styleOrValues) {
        System.out.println(new AutoResetAnsiString(styleOrValues));
    }

    @Override
    public void err(Object... styleOrValues) {
        System.err.println(new AutoResetAnsiString(styleOrValues));
    }
}
