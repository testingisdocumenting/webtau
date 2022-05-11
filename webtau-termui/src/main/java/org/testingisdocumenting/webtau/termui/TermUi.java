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
import org.testingisdocumenting.webtau.reporter.WebTauTest;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;

public class TermUi {
    public static final TermUi INSTANCE = new TermUi();

    private MultiWindowTextGUI gui;
    private ActionListBox listBox;
    private boolean isStarted;
    private BasicWindow window;

    public void start() {
        try {
            registerComponents();
            isStarted = true;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void stop() {
        gui.waitForWindowToClose(window);
    }

    public void registerTest(WebTauTest test) {
        if (!isStarted) {
            start();
        }

        listBox.addItem(test.getScenario(), () -> {});
    }

    private void registerComponents() throws IOException {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        TerminalScreen screen = defaultTerminalFactory.createScreen();
        screen.startScreen();

        gui = new MultiWindowTextGUI(new SeparateTextGUIThread.Factory(), screen);

        window = new BasicWindow("My Root Window");
        window.setHints(Arrays.asList(Window.Hint.FULL_SCREEN, Window.Hint.NO_DECORATIONS));

        Panel contentPanel = new Panel(new GridLayout(2));

        GridLayout gridLayout = (GridLayout)contentPanel.getLayoutManager();
        gridLayout.setHorizontalSpacing(3);

        TermUiWebtauStep step = new TermUiWebtauStep(TokenizedMessage.tokenizedMessage(
                action("action"), OF, stringValue("hello world")));

        Panel stepsPanel = new Panel(new LinearLayout());
        stepsPanel.addComponent(step);
        stepsPanel.setFillColorOverride(TextColor.ANSI.BLACK);

        listBox = new ActionListBox();

        contentPanel.addComponent(listBox);
        contentPanel.addComponent(stepsPanel);

        window.setComponent(contentPanel);

        ((AsynchronousTextGUIThread)gui.getGUIThread()).start();
        gui.addWindow(window);
    }
}
