/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.data.table;

import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.data.table.render.TableRenderStyle;

class PrettyPrintReplTableRenderStyle implements TableRenderStyle {
    private static final String ANSI_COMMA = Color.YELLOW.toString() + ", " + Color.RESET.toString();

    @Override
    public String headerMidLeft() {
        return Color.YELLOW.toString();
    }

    @Override
    public String headerMidMid() {
        return ANSI_COMMA + Color.YELLOW;
    }

    @Override
    public String headerMidRight() {
        return Color.RESET.toString();
    }

    @Override
    public String headerBotLeft() {
        return "*";
    }

    @Override
    public String headerBotMid() {
        return "*";
    }

    @Override
    public String headerBotRight() {
        return "*";
    }

    @Override
    public String headerBotFill() {
        return null;
    }

    @Override
    public String bodyMidLeft() {
        return "";
    }

    @Override
    public String bodyMidMid() {
        return ANSI_COMMA;
    }

    @Override
    public String bodyMidRight() {
        return "";
    }

    @Override
    public String bodyBotLeft() {
        return "";
    }

    @Override
    public String bodyBotMid() {
        return "";
    }

    @Override
    public String bodyBotRight() {
        return "";
    }

    @Override
    public String bodyBotFill() {
        return null;
    }
}
