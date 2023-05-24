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

package org.testingisdocumenting.webtau.browser.page;

import org.testingisdocumenting.webtau.browser.Browser;
import org.testingisdocumenting.webtau.browser.BrowserKeys;

public class BrowserKeysRenderer {
    private static final BrowserKeys keys = Browser.browser.keys;

    private BrowserKeysRenderer() {
    }

    public static String renderKeys(CharSequence sequence) {
        return sequence.toString()
                .replace(keys.enter, "<enter>")
                .replace(keys.tab, "<tab>")
                .replace(keys.backSpace, "<backspace>")
                .replace(keys.shift, "<shift>")
                .replace(keys.escape, "<escape>")
                .replace(keys.end, "<end>")
                .replace(keys.home, "<home>")
                .replace(keys.pageUp, "<pageUp>")
                .replace(keys.pageDown, "<pageDown>")
                .replace(keys.left, "<left>")
                .replace(keys.right, "<right>")
                .replace(keys.up, "<up>")
                .replace(keys.down, "<down>")
                .replace(keys.f1, "<f1>")
                .replace(keys.f2, "<f2>")
                .replace(keys.f3, "<f3>")
                .replace(keys.f4, "<f4>")
                .replace(keys.f5, "<f5>")
                .replace(keys.f6, "<f6>")
                .replace(keys.f7, "<f7>")
                .replace(keys.f8, "<f8>")
                .replace(keys.f9, "<f9>")
                .replace(keys.f10, "<f10>")
                .replace(keys.f11, "<f11>")
                .replace(keys.f12, "<f12>");
    }
}
