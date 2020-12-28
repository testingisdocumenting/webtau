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

package org.testingisdocumenting.webtau.browser;

import org.openqa.selenium.Keys;

import java.util.Arrays;

public class BrowserKeys {
    public final CharSequence NULL = Keys.NULL;
    public final CharSequence CANCEL = Keys.CANCEL;
    public final CharSequence HELP = Keys.HELP;
    public final CharSequence BACK_SPACE = Keys.BACK_SPACE;
    public final CharSequence TAB = Keys.TAB;
    public final CharSequence CLEAR = Keys.CLEAR;
    public final CharSequence RETURN = Keys.RETURN;
    public final CharSequence ENTER = Keys.ENTER;
    public final CharSequence SHIFT = Keys.SHIFT;
    public final CharSequence LEFT_SHIFT = Keys.LEFT_SHIFT;
    public final CharSequence CONTROL = Keys.CONTROL;
    public final CharSequence LEFT_CONTROL = Keys.LEFT_CONTROL;
    public final CharSequence ALT = Keys.ALT;
    public final CharSequence LEFT_ALT = Keys.LEFT_ALT;
    public final CharSequence PAUSE = Keys.PAUSE;
    public final CharSequence ESCAPE = Keys.ESCAPE;
    public final CharSequence SPACE = Keys.SPACE;
    public final CharSequence PAGE_UP = Keys.PAGE_UP;
    public final CharSequence PAGE_DOWN = Keys.PAGE_DOWN;
    public final CharSequence END = Keys.END;
    public final CharSequence HOME = Keys.HOME;
    public final CharSequence LEFT = Keys.LEFT;
    public final CharSequence ARROW_LEFT = Keys.ARROW_LEFT;
    public final CharSequence UP = Keys.UP;
    public final CharSequence ARROW_UP = Keys.ARROW_UP;
    public final CharSequence RIGHT = Keys.RIGHT;
    public final CharSequence ARROW_RIGHT = Keys.ARROW_RIGHT;
    public final CharSequence DOWN = Keys.DOWN;
    public final CharSequence ARROW_DOWN = Keys.ARROW_DOWN;
    public final CharSequence INSERT = Keys.INSERT;
    public final CharSequence DELETE = Keys.DELETE;
    public final CharSequence SEMICOLON = Keys.SEMICOLON;
    public final CharSequence EQUALS = Keys.EQUALS;

    public final CharSequence NUMPAD0 = Keys.NUMPAD0;
    public final CharSequence NUMPAD1 = Keys.NUMPAD1;
    public final CharSequence NUMPAD2 = Keys.NUMPAD2;
    public final CharSequence NUMPAD3 = Keys.NUMPAD3;
    public final CharSequence NUMPAD4 = Keys.NUMPAD4;
    public final CharSequence NUMPAD5 = Keys.NUMPAD5;
    public final CharSequence NUMPAD6 = Keys.NUMPAD6;
    public final CharSequence NUMPAD7 = Keys.NUMPAD7;
    public final CharSequence NUMPAD8 = Keys.NUMPAD8;
    public final CharSequence NUMPAD9 = Keys.NUMPAD9;
    public final CharSequence MULTIPLY = Keys.MULTIPLY;
    public final CharSequence ADD = Keys.ADD;
    public final CharSequence SEPARATOR = Keys.SEPARATOR;
    public final CharSequence SUBTRACT = Keys.SUBTRACT;
    public final CharSequence DECIMAL = Keys.DECIMAL;
    public final CharSequence DIVIDE = Keys.DIVIDE;

    public final CharSequence F1 = Keys.F1;
    public final CharSequence F2 = Keys.F2;
    public final CharSequence F3 = Keys.F3;
    public final CharSequence F4 = Keys.F4;
    public final CharSequence F5 = Keys.F5;
    public final CharSequence F6 = Keys.F6;
    public final CharSequence F7 = Keys.F7;
    public final CharSequence F8 = Keys.F8;
    public final CharSequence F9 = Keys.F9;
    public final CharSequence F10 = Keys.F10;
    public final CharSequence F11 = Keys.F11;
    public final CharSequence F12 = Keys.F12;
    public final CharSequence META = Keys.META;
    public final CharSequence COMMAND = Keys.COMMAND;
    public final CharSequence ZENKAKU_HANKAKU = Keys.ZENKAKU_HANKAKU;

    public String chord(CharSequence... value) {
        return Keys.chord(Arrays.asList(value));
    }
}
