/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.webtau.pdf;

import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.expectation.ActualPathAndDescriptionAware;
import org.testingisdocumenting.webtau.expectation.ActualValueExpectations;

public class PdfText implements ActualValueExpectations, ActualPathAndDescriptionAware {
    private final String id;
    private final String text;

    public PdfText(String id, String text) {
        this.id = id;
        this.text = text.replace("\r", "");
    }

    public String getText() {
        return text;
    }

    @Override
    public ActualPath actualPath() {
        return new ActualPath(id);
    }
}
