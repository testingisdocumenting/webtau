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

package com.twosigma.webtau.browser;

import com.twosigma.webtau.utils.ResourceUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;

public class InjectedJavaScript {
    private final String injectionScript = ResourceUtils.textContent("browser/injection.js");

    private JavascriptExecutor javascriptExecutor;

    InjectedJavaScript(JavascriptExecutor javascriptExecutor) {
        this.javascriptExecutor = javascriptExecutor;
    }

    public void flashWebElements(List<WebElement> webElements) {
        injectScript();
        javascriptExecutor.executeScript("window._webtau.flashElements(arguments[0])", webElements);
    }

    private void injectScript() {
        javascriptExecutor.executeScript(injectionScript);
    }
}
