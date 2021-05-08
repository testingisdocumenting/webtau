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

package org.testingisdocumenting.webtau.featuretesting

import org.testingisdocumenting.webtau.http.testserver.FixedResponsesHandler
import org.testingisdocumenting.webtau.utils.ResourceUtils

class WebTauBrowserFeaturesTestData {
    static void registerEndPoints(FixedResponsesHandler handler) {
        handler.registerGet("/search", htmlResponse('search.html'))
        handler.registerGet("/forms", htmlResponse('forms.html'))
        handler.registerGet("/special-forms", htmlResponse('special-forms.html'))
        handler.registerGet("/calculation", htmlResponse('calculation.html'))
        handler.registerGet("/finders-and-filters", htmlResponse('finders-and-filters.html'))
        handler.registerGet("/drag-and-drop-html5", htmlResponse('drag-and-drop-html5.html'))
        handler.registerGet("/drag-and-drop-jquery", htmlResponse('drag-and-drop-jquery.html'))
        handler.registerGet("/element-actions", htmlResponse('element-actions.html'))
        handler.registerGet("/cookies", htmlResponse('cookies.html'))
        handler.registerGet("/matchers", htmlResponse('matchers.html'))
        handler.registerGet("/local-storage", htmlResponse('local-storage.html'))
        handler.registerGet("/logged-in-user", htmlResponse('logged-in-user.html'))
        handler.registerGet("/flicking-element", htmlResponse('flicking-element.html'))
        handler.registerGet("/resource-creation", htmlResponse('resource-creation.html'))
        handler.registerGet("/ag-grid-multi-select", htmlResponse('ag-grid-multi-select.html'))
        handler.registerGet("/scrolls", htmlResponse('scrolls.html'))
    }

    private static TestServerHtmlResponse htmlResponse(String resourceName) {
        new TestServerHtmlResponse(ResourceUtils.textContent(resourceName))
    }
}