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

import org.testingisdocumenting.webtau.cfg.WebTauConfig;
import org.testingisdocumenting.webtau.server.route.WebtauRouter;

import java.nio.file.Path;
import java.nio.file.Paths;

public class WebtauServerFacade {
    public static final WebtauServerFacade server = new WebtauServerFacade();

    private WebtauServerFacade() {
    }

    public WebtauServer serve(String serverId, String path) {
        return serve(serverId, path, 0);
    }

    public WebtauServer serve(String serverId, Path path) {
        return serve(serverId, path, 0);
    }

    public WebtauServer serve(String serverId, String path, int port) {
        return serve(serverId, Paths.get(path), port);
    }

    public WebtauServer serve(String serverId, Path path, int port) {
        WebtauStaticServer server = new WebtauStaticServer(serverId, WebTauConfig.getCfg().fullPath(path), port);
        server.start();

        return server;
    }

    public WebtauServer proxy(String serverId, String urlToProxy, int port) {
        WebtauProxyServer server = new WebtauProxyServer(serverId, urlToProxy, port);
        server.start();

        return server;
    }

    public WebtauRouter router(String id) {
        return new WebtauRouter(id);
    }
}
