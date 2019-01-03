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

package com.twosigma.webtau;

import com.twosigma.webtau.browser.Browser;
import com.twosigma.webtau.browser.expectation.DisabledValueMatcher;
import com.twosigma.webtau.browser.expectation.EnabledValueMatcher;
import com.twosigma.webtau.browser.expectation.HiddenValueMatcher;
import com.twosigma.webtau.browser.expectation.VisibleValueMatcher;
import com.twosigma.webtau.browser.page.PageElement;
import com.twosigma.webtau.cache.Cache;
import com.twosigma.webtau.cfg.WebTauConfig;
import com.twosigma.webtau.data.Data;
import com.twosigma.webtau.expectation.ValueMatcher;
import com.twosigma.webtau.http.Http;
import com.twosigma.webtau.http.datanode.DataNode;
import com.twosigma.webtau.pdf.Pdf;
import com.twosigma.webtau.schema.expectation.SchemaMatcher;

public class WebTauDsl extends Ddjt {
    public static final Data data = Data.data;
    public static final Cache cache = Cache.cache;

    public static final Http http = Http.http;
    public static final Browser browser = Browser.browser;

    public static WebTauConfig getCfg() {
        return WebTauConfig.getCfg();
    }

    public static Pdf pdf(DataNode node) {
        return Pdf.pdf(node);
    }

    public static PageElement $(String css) {
        return browser.$(css);
    }

    public static ValueMatcher beVisible() {
        return new VisibleValueMatcher();
    }

    public static ValueMatcher beHidden() {
        return new HiddenValueMatcher();
    }

    public static ValueMatcher beEnabled() {
        return new EnabledValueMatcher();
    }

    public static ValueMatcher beDisabled() {
        return new DisabledValueMatcher();
    }

    public static ValueMatcher getBeVisible() {
        return beVisible();
    }

    public static SchemaMatcher complyWithSchema(String schemaFileName) {
        return new SchemaMatcher(schemaFileName);
    }

    public static SchemaMatcher beCompliantWithSchema(String schemaFileName) {
        return complyWithSchema(schemaFileName);
    }
}
