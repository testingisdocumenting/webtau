/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.expectation.equality;

import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.utils.StringUtils;

public class Mismatch {
    private ActualPath actualPath;
    private String message;
    private String fullMessage;

    public Mismatch(ActualPath actualPath, String message) {
        this.actualPath = actualPath;
        this.message = message;
        this.fullMessage = StringUtils.concatWithIndentation(actualPath.getPath() + ": ", message);
    }

    public ActualPath getActualPath() {
        return actualPath;
    }

    public String getMessage() {
        return message;
    }

    public String fullMessage() {
        return fullMessage;
    }

    @Override
    public String toString() {
        return fullMessage();
    }
}
