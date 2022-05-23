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

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.io.IOException;
import java.util.Arrays;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;

public class TermUiTestApp {
    public static void main(String[] args) throws IOException, InterruptedException {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        TerminalScreen screen = defaultTerminalFactory.createScreen();
        screen.startScreen();

        MultiWindowTextGUI gui = new MultiWindowTextGUI(new SeparateTextGUIThread.Factory(), screen);

        BasicWindow window = new BasicWindow("My Root Window");
        window.setHints(Arrays.asList(Window.Hint.FULL_SCREEN, Window.Hint.NO_DECORATIONS));

        Panel contentPanel = new Panel(new GridLayout(2));

        GridLayout gridLayout = (GridLayout)contentPanel.getLayoutManager();
        gridLayout.setHorizontalSpacing(3);

        Label title = new Label("This is a label that new line");
        Label anotherLabel = new Label("More text in\nmultiple lines");

        TermUiWebTauStep step = new TermUiWebTauStep(TokenizedMessage.tokenizedMessage(
                action("action"), OF, stringValue("hello world")));

        Panel stepsPanel = new Panel(new LinearLayout());
        stepsPanel.addComponent(step);
        stepsPanel.setFillColorOverride(TextColor.ANSI.BLACK);

        title.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.BEGINNING, // Horizontal alignment in the grid cell if the cell is larger than the component's preferred size
                GridLayout.Alignment.BEGINNING, // Vertical alignment in the grid cell if the cell is larger than the component's preferred size
                true,       // Give the component extra horizontal space if available
                false,        // Give the component extra vertical space if available
                2,                  // Horizontal span
                1));                  // Vertical span

        ActionListBox listBox = new ActionListBox();
        listBox.addItem("[test-file-one]", () -> {});
        listBox.addItem("  scenario two\nmultiple lines", () -> {});
        listBox.addItem("  scenario three", () -> {
            listBox.addItem("new item", () -> {
            });
        });

        contentPanel.addComponent(listBox);
        contentPanel.addComponent(stepsPanel);

        window.setComponent(contentPanel);

        ((AsynchronousTextGUIThread)gui.getGUIThread()).start();
        gui.addWindowAndWait(window);
    }
}
