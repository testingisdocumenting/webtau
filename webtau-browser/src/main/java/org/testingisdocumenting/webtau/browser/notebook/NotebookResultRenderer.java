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

package org.testingisdocumenting.webtau.browser.notebook;

import org.testingisdocumenting.webtau.browser.page.PageElement;
import org.testingisdocumenting.webtau.notebook.PrettyPrintableToHtmlConverter;

public class NotebookResultRenderer {
    private NotebookResultRenderer() {
    }

    /**
     * highlights element when run from Notebook
     * @param pageElement element to display and highlight
     * @return HTML rendered with element info
     */
    public static String renderPageElementAndHighlight(PageElement pageElement) {
        pageElement.highlight();
        return PrettyPrintableToHtmlConverter.convert(pageElement);
    }
}