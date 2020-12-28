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
    public final CharSequence cancel = Keys.CANCEL;
    public final CharSequence help = Keys.HELP;
    public final CharSequence backSpace = Keys.BACK_SPACE;
    public final CharSequence tab = Keys.TAB;
    public final CharSequence clear = Keys.CLEAR;
    public final CharSequence enter = Keys.ENTER;
    public final CharSequence shift = Keys.SHIFT;
    public final CharSequence leftShift = Keys.LEFT_SHIFT;
    public final CharSequence control = Keys.CONTROL;
    public final CharSequence leftControl = Keys.LEFT_CONTROL;
    public final CharSequence alt = Keys.ALT;
    public final CharSequence leftAlt = Keys.LEFT_ALT;
    public final CharSequence pause = Keys.PAUSE;
    public final CharSequence escape = Keys.ESCAPE;
    public final CharSequence space = Keys.SPACE;
    public final CharSequence pageUp = Keys.PAGE_UP;
    public final CharSequence pageDown = Keys.PAGE_DOWN;
    public final CharSequence end = Keys.END;
    public final CharSequence home = Keys.HOME;
    public final CharSequence left = Keys.LEFT;
    public final CharSequence arrowLeft = Keys.ARROW_LEFT;
    public final CharSequence up = Keys.UP;
    public final CharSequence arrowUp = Keys.ARROW_UP;
    public final CharSequence right = Keys.RIGHT;
    public final CharSequence arrowRight = Keys.ARROW_RIGHT;
    public final CharSequence down = Keys.DOWN;
    public final CharSequence arrowDown = Keys.ARROW_DOWN;
    public final CharSequence insert = Keys.INSERT;
    public final CharSequence delete = Keys.DELETE;
    public final CharSequence semicolon = Keys.SEMICOLON;
    public final CharSequence equals = Keys.EQUALS;

    public final CharSequence numpad0 = Keys.NUMPAD0;
    public final CharSequence numpad1 = Keys.NUMPAD1;
    public final CharSequence numpad2 = Keys.NUMPAD2;
    public final CharSequence numpad3 = Keys.NUMPAD3;
    public final CharSequence numpad4 = Keys.NUMPAD4;
    public final CharSequence numpad5 = Keys.NUMPAD5;
    public final CharSequence numpad6 = Keys.NUMPAD6;
    public final CharSequence numpad7 = Keys.NUMPAD7;
    public final CharSequence numpad8 = Keys.NUMPAD8;
    public final CharSequence numpad9 = Keys.NUMPAD9;
    public final CharSequence multiply = Keys.MULTIPLY;
    public final CharSequence add = Keys.ADD;
    public final CharSequence separator = Keys.SEPARATOR;
    public final CharSequence subtract = Keys.SUBTRACT;
    public final CharSequence decimal = Keys.DECIMAL;
    public final CharSequence divide = Keys.DIVIDE;

    public final CharSequence f1 = Keys.F1;
    public final CharSequence f2 = Keys.F2;
    public final CharSequence f3 = Keys.F3;
    public final CharSequence f4 = Keys.F4;
    public final CharSequence f5 = Keys.F5;
    public final CharSequence f6 = Keys.F6;
    public final CharSequence f7 = Keys.F7;
    public final CharSequence f8 = Keys.F8;
    public final CharSequence f9 = Keys.F9;
    public final CharSequence f10 = Keys.F10;
    public final CharSequence f11 = Keys.F11;
    public final CharSequence f12 = Keys.F12;
    public final CharSequence meta = Keys.META;
    public final CharSequence command = Keys.COMMAND;
    public final CharSequence zenkakuHankaku = Keys.ZENKAKU_HANKAKU;

    public String chord(CharSequence... value) {
        return Keys.chord(Arrays.asList(value));
    }
}
