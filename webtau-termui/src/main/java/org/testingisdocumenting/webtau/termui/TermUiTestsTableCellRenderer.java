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

import com.googlecode.lanterna.gui2.TextGUIGraphics;
import com.googlecode.lanterna.gui2.table.DefaultTableCellRenderer;
import com.googlecode.lanterna.gui2.table.Table;

public class TermUiTestsTableCellRenderer extends DefaultTableCellRenderer<Object> {
    // overriding to use ansi sequence string render to enable colors
    protected void render(Table<Object> table, Object cell, int columnIndex, int rowIndex, boolean isSelected,
                          TextGUIGraphics textGUIGraphics) {
        String[] lines = getContent(cell);
        int rowCount = 0;
        for(String line: lines) {
            textGUIGraphics.putCSIStyledString(0, rowCount++, line);
        }
    }
}
