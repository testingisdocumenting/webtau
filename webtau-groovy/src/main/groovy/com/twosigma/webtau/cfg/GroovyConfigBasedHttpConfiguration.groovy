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

package com.twosigma.webtau.cfg

import com.twosigma.webtau.http.HttpRequestHeader
import com.twosigma.webtau.http.config.HttpConfiguration
import com.twosigma.webtau.http.config.HttpConfigurations

import java.util.concurrent.atomic.AtomicReference

class GroovyConfigBasedHttpConfiguration implements HttpConfiguration {
    private static AtomicReference<Closure> headerProvider = new AtomicReference<>()

    GroovyConfigBasedHttpConfiguration() {
    }

    @Override
    String fullUrl(String url) {
        return url
    }

    static void setHeaderProvider(Closure headerProvider) {
        this.headerProvider.set(headerProvider)
    }

    @Override
    HttpRequestHeader fullHeader(HttpRequestHeader given) {
        if (!headerProvider.get()) {
            return given
        }

        return HttpConfigurations.withDisabledConfigurations {
            return headerProvider.get().call(given) as HttpRequestHeader
        }
    }
}
