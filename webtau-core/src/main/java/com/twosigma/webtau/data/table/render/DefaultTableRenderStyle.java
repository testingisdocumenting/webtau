/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.data.table.render;

public class DefaultTableRenderStyle implements TableRenderStyle {
    @Override
    public String headerMidLeft() {
        return ":";
    }

    @Override
    public String headerMidMid() {
        return "|";
    }

    @Override
    public String headerMidRight() {
        return ":";
    }

    public String headerBotLeft() {
        return ".";
    }

    public String headerBotMid() {
        return ".";
    }

    public String headerBotRight() {
        return ".";
    }

    public String headerBotFill() {
        return "_";
    }

    @Override
    public String bodyMidLeft() {
        return "|";
    }

    @Override
    public String bodyMidMid() {
        return "|";
    }

    @Override
    public String bodyMidRight() {
        return "|";
    }

    @Override
    public String bodyBotLeft() {
        return ".";
    }

    @Override
    public String bodyBotMid() {
        return ".";
    }

    @Override
    public String bodyBotRight() {
        return "|";
    }

    @Override
    public String bodyBotFill() {
        return "_";
    }
}
