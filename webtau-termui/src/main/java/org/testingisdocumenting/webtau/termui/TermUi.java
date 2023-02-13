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

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import org.apache.commons.lang3.StringUtils;
import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.console.ansi.FontStyle;
import org.testingisdocumenting.webtau.reporter.TestStatus;
import org.testingisdocumenting.webtau.reporter.WebTauTest;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TermUi {
    public static final TermUi INSTANCE = new TermUi();

    private MultiWindowTextGUI gui;
    private boolean isStarted;
    private BasicWindow window;
    private Table<Object> table;

    private final Map<String, Integer> rowIdxByTestId = new HashMap<>();

    public void start() {
        try {
            registerComponents();
            isStarted = true;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void stop() {
    }

    synchronized public void registerTest(WebTauTest test) {
        if (!isStarted) {
            start();
        }

        TableModel<Object> model = table.getTableModel();
        int lastIdx = model.getRowCount();
        model.addRow(padStatus(""), test.getShortContainerId(), test.getScenario());

        rowIdxByTestId.put(test.getId(), lastIdx);
    }

    synchronized public void updateTest(WebTauTest test) {
        TableModel<Object> model = table.getTableModel();

        Integer rowIdx = rowIdxByTestId.get(test.getId());

        model.setCell(0, rowIdx, padStatus(statusToText(test.getTestStatus())));
    }

    private String statusToText(TestStatus testStatus) {
        switch (testStatus) {
            case Failed:
                return Color.RED + "Failed" + FontStyle.RESET;
            case Errored:
                return Color.RED + "Errored" + FontStyle.RESET;
            case Passed:
                return Color.GREEN + "Passed" + FontStyle.RESET;
        }

        return testStatus.toString();
    }

    private String padStatus(String status) {
        return StringUtils.leftPad(status, 8);
    }

    private void registerComponents() throws IOException {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        TerminalScreen screen = defaultTerminalFactory.createScreen();
        screen.startScreen();

        gui = new MultiWindowTextGUI(new SeparateTextGUIThread.Factory(), screen);

        window = new BasicWindow("My Root Window");
        window.setHints(Arrays.asList(Window.Hint.FULL_SCREEN, Window.Hint.NO_DECORATIONS));

        Panel contentPanel = new Panel(new GridLayout(1));

        table = new Table<>("State", "Group", "Scenario");
        table.setTableCellRenderer(new TermUiTestsTableCellRenderer());
        table.setPreferredSize(screen.getTerminalSize());
        contentPanel.addComponent(table);

        window.setComponent(contentPanel);

        AsynchronousTextGUIThread guiThread = (AsynchronousTextGUIThread) gui.getGUIThread();
        gui.addWindow(window);

        guiThread.start();
    }
}
