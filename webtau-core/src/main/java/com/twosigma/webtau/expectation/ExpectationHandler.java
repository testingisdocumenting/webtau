package com.twosigma.webtau.expectation;

public interface ExpectationHandler {
    enum Flow {
        Terminate,
        PassToNext
    }

    Flow onValueMismatch(ActualPath actualPath, Object actualValue, String message);
//    Flow onCodeMismatch(ActualPath actualPath, CodeBlock codeBlock, String message);
}
