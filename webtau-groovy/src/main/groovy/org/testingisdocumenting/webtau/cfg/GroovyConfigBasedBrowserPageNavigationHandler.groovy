/*
 * Copyright 2021 webtau maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.webtau.cfg

import org.testingisdocumenting.webtau.browser.navigation.BrowserPageNavigationHandler

import java.util.concurrent.atomic.AtomicReference

class GroovyConfigBasedBrowserPageNavigationHandler implements BrowserPageNavigationHandler {
    private static AtomicReference<Closure> handler = new AtomicReference<>()

    GroovyConfigBasedBrowserPageNavigationHandler() {
    }

    static void setHandler(Closure handler) {
        this.handler.set(handler)
    }

    @Override
    void onOpenedPage(String passedUrl, String fullUrl, String currentUrl) {
        if (!handler.get()) {
            return
        }

        handler.get().call(passedUrl, fullUrl, currentUrl)
    }
}
