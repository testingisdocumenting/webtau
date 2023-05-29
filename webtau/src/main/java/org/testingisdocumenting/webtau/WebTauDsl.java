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

package org.testingisdocumenting.webtau;

import org.testingisdocumenting.webtau.browser.Browser;
import org.testingisdocumenting.webtau.browser.expectation.DisabledValueMatcher;
import org.testingisdocumenting.webtau.browser.expectation.EnabledValueMatcher;
import org.testingisdocumenting.webtau.browser.expectation.HiddenValueMatcher;
import org.testingisdocumenting.webtau.browser.expectation.VisibleValueMatcher;
import org.testingisdocumenting.webtau.browser.page.PageElement;
import org.testingisdocumenting.webtau.cache.Cache;
import org.testingisdocumenting.webtau.cfg.WebTauConfig;
import org.testingisdocumenting.webtau.cli.Cli;
import org.testingisdocumenting.webtau.data.Data;
import org.testingisdocumenting.webtau.db.Database;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;
import org.testingisdocumenting.webtau.fs.FileSystem;
import org.testingisdocumenting.webtau.graphql.GraphQL;
import org.testingisdocumenting.webtau.http.Http;
import org.testingisdocumenting.webtau.schema.expectation.SchemaMatcher;
import org.testingisdocumenting.webtau.server.WebTauServerFacade;
import org.testingisdocumenting.webtau.websocket.WebSocket;

/*
Convenient class for static * import
 */
public class WebTauDsl extends WebTauCore {
    public static final FileSystem fs = FileSystem.fs;
    public static final Data data = Data.data;
    public static final Cache cache = Cache.cache;

    public static final Http http = Http.http;
    public static final WebSocket websocket = WebSocket.websocket;
    public static final Browser browser = Browser.browser;
    public static final Cli cli = Cli.cli;
    public static final Database db = Database.db;
    public static final GraphQL graphql = GraphQL.graphql;
    public static final WebTauServerFacade server = WebTauServerFacade.server;

    /**
     * visible matcher to check if UI element is visible
     * @see #hidden
     */
    public static final ValueMatcher visible = new VisibleValueMatcher();

    /**
     * hidden matcher to check if UI element is hidden
     * @see #visible
     */
    public static final ValueMatcher hidden = new HiddenValueMatcher();

    /**
     * enabled matcher to check if UI element is enabled
     * @see #disabled
     */
    public static final ValueMatcher enabled = new EnabledValueMatcher();

    /**
     * disabled matcher to check if UI element is disabled
     * @see #enabled
     */
    public static final ValueMatcher disabled = new DisabledValueMatcher();

    public static WebTauConfig cfg = WebTauConfig.getCfg();

    public static PageElement $(String css) {
        return browser.$(css);
    }

    /**
     * check if DataNode complies with schema
     * @param schemaFileName schema file name
     * @return schema matcher
     */
    public static SchemaMatcher complyWithSchema(String schemaFileName) {
        return new SchemaMatcher(schemaFileName);
    }
}
