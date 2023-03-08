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

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import org.testingisdocumenting.webtau.reporter.TokenizedMessageToAnsiConverter;

import java.util.List;
import java.util.stream.Collectors;

public class TermUiWebTauStepRenderer implements ComponentRenderer<TermUiWebTauStep> {
    @Override
    public TerminalSize getPreferredSize(TermUiWebTauStep component) {
        return new TerminalSize(60, 20); // TODO
    }

    @Override
    public void drawComponent(TextGUIGraphics graphics, TermUiWebTauStep component) {
        List<Object> ansiParts = TokenizedMessageToAnsiConverter.DEFAULT.convert(component.getMessage(), 0);

        TerminalSize size = graphics.getSize();
        String text = ansiParts.stream().map(Object::toString).collect(Collectors.joining());
        graphics.putCSIStyledString(1, 1, text);
    }
}
