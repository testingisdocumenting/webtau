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

package org.testingisdocumenting.webtau.app.repl.tabledata

import org.apache.commons.lang3.StringUtils
import org.testingisdocumenting.webtau.console.ansi.Color
import org.testingisdocumenting.webtau.data.render.DataRenderers
import org.testingisdocumenting.webtau.data.table.render.TableCellDataRenderer

class ReplTableCellDataRenderer implements TableCellDataRenderer {
    @Override
    String renderCell(Object value) {
        return DataRenderers.render(value)
    }

    @Override
    Integer valueWidth(Object value) {
        return DataRenderers.render(value).length()
    }

    @Override
    String wrapBeforeRender(Object original, String rendered) {
        if (original == null) {
            return Color.YELLOW.toString() + rendered + Color.RESET.toString()
        }

        if (original instanceof Number) {
            return Color.CYAN.toString() + rendered + Color.RESET.toString()
        }

        return rendered
    }

    @Override
    String align(Object original, String rendered, Integer width, String fill) {
        if (original instanceof Number) {
            return StringUtils.leftPad(rendered, width, fill)
        }

        return StringUtils.rightPad(rendered, width, fill)
    }

    @Override
    boolean useDefaultWidth() {
        return true
    }
}
