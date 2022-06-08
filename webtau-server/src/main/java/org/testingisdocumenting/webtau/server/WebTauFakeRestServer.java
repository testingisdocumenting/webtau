/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.server;

import org.eclipse.jetty.server.Handler;
import org.testingisdocumenting.webtau.server.route.WebTauRouter;

import java.util.Collections;
import java.util.Map;

public class WebTauFakeRestServer extends WebTauJettyServer {
    public WebTauFakeRestServer(String id, int passedPort) {
        super(id, passedPort);
    }

    public WebTauFakeRestServer(String id, int passedPort, WebTauRouter router) {
        super(id, passedPort);
        addOverride(router);
    }

    @Override
    protected Map<String, Object> provideStepInput() {
        return Collections.emptyMap();
    }

    @Override
    protected void validateParams() {
    }

    @Override
    protected Handler createJettyHandler() {
        return new WebTauServerFakeJettyHandler(serverId);
    }

    @Override
    public String getType() {
        return "fake-rest";
    }
}
