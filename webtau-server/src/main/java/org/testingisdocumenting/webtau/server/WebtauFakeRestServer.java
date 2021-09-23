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
import org.testingisdocumenting.webtau.server.route.WebtauRouter;

import java.util.Collections;
import java.util.Map;

public class WebtauFakeRestServer extends WebtauJettyServer {
    public WebtauFakeRestServer(String id, int passedPort) {
        super(id, passedPort);
    }

    public WebtauFakeRestServer(String id, int passedPort, WebtauRouter router) {
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
        return new WebtauServerFakeJettyHandler(serverId);
    }

    @Override
    public String getType() {
        return "fake-rest";
    }
}
