/*
 * Copyright 2020 webtau maintainers
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

import org.testingisdocumenting.webtau.http.HttpHeader
import org.testingisdocumenting.webtau.http.config.WebTauHttpConfiguration
import org.testingisdocumenting.webtau.http.config.WebTauHttpConfigurations
import org.testingisdocumenting.webtau.http.validation.HttpValidationHandlers

import java.util.concurrent.atomic.AtomicReference

class GroovyConfigBasedHttpConfiguration implements WebTauHttpConfiguration {
    private static AtomicReference<Closure> headerProvider = new AtomicReference<>()

    GroovyConfigBasedHttpConfiguration() {
    }

    static void setHeaderProvider(Closure headerProvider) {
        this.headerProvider.set(headerProvider)
    }

    static void clear() {
        headerProvider.set(null)
    }

    @Override
    HttpHeader fullHeader(String fullUrl, String passedUrl, HttpHeader given) {
        if (!headerProvider.get()) {
            return given
        }

        return WebTauHttpConfigurations.withDisabledConfigurations {
            HttpValidationHandlers.withDisabledHandlers {
                return headerProvider.get().call(fullUrl, passedUrl, given) as HttpHeader
            }
        }
    }
}
