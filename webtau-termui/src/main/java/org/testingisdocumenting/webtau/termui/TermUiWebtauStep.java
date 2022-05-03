/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.termui;

import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

public class TermUiWebtauStep extends AbstractComponent<TermUiWebtauStep> {
    private final TokenizedMessage message;

    public TermUiWebtauStep(TokenizedMessage message) {
        this.message = message;
    }

    public TokenizedMessage getMessage() {
        return message;
    }

    @Override
    protected ComponentRenderer<TermUiWebtauStep> createDefaultRenderer() {
        return new TermUiWebtauStepRenderer();
    }
}
