/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.cli.repl.tabledata

import org.testingisdocumenting.webtau.console.ansi.Color
import org.testingisdocumenting.webtau.data.table.render.TableRenderStyle
import org.testingisdocumenting.webtau.data.table.render.TableRenderer

class ReplTableRenderStyle implements TableRenderStyle {
    private static final String ANSI_COMMA = Color.YELLOW.toString() + ", " + Color.RESET.toString()

    @Override
    String headerMidLeft() {
        return Color.YELLOW
    }

    @Override
    String headerMidMid() {
        return ANSI_COMMA + Color.YELLOW
    }

    @Override
    String headerMidRight() {
        return Color.RESET
    }

    @Override
    String headerBotLeft() {
        return "*"
    }

    @Override
    String headerBotMid() {
        return "*"
    }

    @Override
    String headerBotRight() {
        return "*"
    }

    @Override
    String headerBotFill() {
        return null
    }

    @Override
    String bodyMidLeft() {
        return ""
    }

    @Override
    String bodyMidMid() {
        return ANSI_COMMA
    }

    @Override
    String bodyMidRight() {
        return ""
    }

    @Override
    String bodyBotLeft() {
        return ""
    }

    @Override
    String bodyBotMid() {
        return ""
    }

    @Override
    String bodyBotRight() {
        return ""
    }

    @Override
    String bodyBotFill() {
        return null
    }
}
