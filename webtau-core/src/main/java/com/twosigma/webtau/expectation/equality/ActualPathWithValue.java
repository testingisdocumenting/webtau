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

import com.twosigma.webtau.data.render.DataRenderers;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.utils.StringUtils;

public class ActualPathWithValue {
    private ActualPath actualPath;
    private Object value;
    private String fullMessage;

    public ActualPathWithValue(ActualPath actualPath, Object value) {
        this.actualPath = actualPath;
        this.value = value;
        this.fullMessage = StringUtils.concatWithIndentation(actualPath.getPath() + ": ", DataRenderers.render(value));
    }

    public ActualPath getActualPath() {
        return actualPath;
    }

    public Object getValue() {
        return value;
    }

    public String getFullMessage() {
        return fullMessage;
    }

    @Override
    public String toString() {
        return getFullMessage();
    }
}
