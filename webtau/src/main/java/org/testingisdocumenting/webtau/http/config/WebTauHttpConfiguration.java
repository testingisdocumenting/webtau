/*
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

package org.testingisdocumenting.webtau.http.config;

import org.testingisdocumenting.webtau.http.HttpHeader;
import org.testingisdocumenting.webtau.utils.UrlUtils;

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg;

public class WebTauHttpConfiguration implements HttpConfiguration {
    @Override
    public String fullUrl(String url) {
        if (UrlUtils.isFull(url)) {
            return url;
        }

        return UrlUtils.concat(getCfg().getBaseUrl(), url);
    }

    @Override
    public HttpHeader fullHeader(String fullUrl, String passedUrl, HttpHeader given) {
        return given;
    }
}
