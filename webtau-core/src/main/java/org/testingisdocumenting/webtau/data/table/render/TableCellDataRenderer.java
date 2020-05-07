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

package org.testingisdocumenting.webtau.data.table.render;

import org.apache.commons.lang3.StringUtils;

public interface TableCellDataRenderer {
    String renderCell(Object value);

    default Integer valueWidth(Object value) {
        throw new UnsupportedOperationException("not implemented");
    }

    default boolean useDefaultWidth() {
        return true;
    }

    default String wrapBeforeRender(Object original, String rendered) {
        return rendered;
    }

    default String align(Object original, String rendered, Integer width, String fill) {
        return StringUtils.rightPad(rendered, width, fill);
    }
}
