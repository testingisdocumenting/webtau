/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.data.render;

import org.testingisdocumenting.webtau.console.ansi.Color;

public class PrettyPrinterDecorationToken {
    public static final PrettyPrinterDecorationToken EMPTY = new PrettyPrinterDecorationToken("", Color.RESET);

    private final String wrapWith;
    private final Color color;

    public PrettyPrinterDecorationToken(String wrapWith, Color color) {
        this.wrapWith = wrapWith;
        this.color = color;
    }

    public String getWrapWith() {
        return wrapWith;
    }

    public Color getColor() {
        return color;
    }

    public boolean isEmpty() {
        return wrapWith.isEmpty();
    }
}
