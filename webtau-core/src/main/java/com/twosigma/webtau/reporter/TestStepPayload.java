package com.twosigma.webtau.reporter;

import java.util.Map;

public interface TestStepPayload {
    Map<String, ?> toMap();
}
