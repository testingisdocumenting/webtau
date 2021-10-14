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

import static org.testingisdocumenting.webtau.server.WebtauServerResponseBuilder.*;

public class WebtauServerFacade {
    public static final WebtauServerFacade server = new WebtauServerFacade();

    private WebtauServerFacade() {
    }

    public final WebtauServerResponseBuilder response = new WebtauServerResponseBuilder();

    /**
     * create response for a server based on the payload type. For String the response will be <code>plain/text</code> and for
     * map/list/beans it will be application/json.
     *
     * @see WebtauServerFacade#response
     * @see WebtauServerResponseBuilder#text
     * @param body body to serialzie as response
     * @return response instance
     */
    public WebtauServerResponse response(Object body) {
        return response(USE_DEFAULT_STATUS_CODE, body);
    }

    /**
     * create response for a server based on the payload type with a specified status code.
     * For String the response will be <code>plain/text</code> and for
     * map/list/beans it will be application/json.
     *
     * @see WebtauServerFacade#response
     * @see WebtauServerResponseBuilder#text
     * @param body body to serialzie as response
     * @return response instance
     */
    public WebtauServerResponse response(int statusCode, Object body) {
        if (body instanceof String) {
            return response.text(statusCode, body.toString());
        }

        if (body == null) {
            return response.text(statusCode, "");
        }

        return response.json(statusCode, body);
    }

    /**
     * creates static content server and starts it on a random part
     * @param serverId unique server id
     * @param path static content path
     * @return server instance
     */
    public WebtauServer serve(String serverId, String path) {
        return serve(serverId, path, 0);
    }

    /**
     * creates static content server and starts it on a random part
     * @param serverId unique server id
     * @param path static content path
     * @return server instance
     */
    public WebtauServer serve(String serverId, Path path) {
        return serve(serverId, path, 0);
    }

    /**
     * creates static content server and starts it on the specified part
     * @param serverId unique server id
     * @param path static content path
     * @param port server port
     * @return server instance
     */
    public WebtauServer serve(String serverId, String path, int port) {
        return serve(serverId, Paths.get(path), port);
    }

    /**
     * creates static content server and starts it on the specified part
     * @param serverId unique server id
     * @param path static content path
     * @param port server port
     * @return server instance
     */
    public WebtauServer serve(String serverId, Path path, int port) {
        WebtauStaticServer server = new WebtauStaticServer(serverId, WebTauConfig.getCfg().fullPath(path), port);
        server.start();

        return server;
    }

    /**
     * creates proxy server and starts it on the specified part
     * @param serverId unique server id
     * @param urlToProxy url to proxy to
     * @param port server port
     * @return server instance
     */
    public WebtauServer proxy(String serverId, String urlToProxy, int port) {
        WebtauProxyServer server = new WebtauProxyServer(serverId, urlToProxy, port);
        server.start();

        return server;
    }

    /**
     * creates proxy server and starts it on a random part
     * @param serverId unique server id
     * @param urlToProxy url to proxy to
     * @return server instance
     */
    public WebtauServer proxy(String serverId, String urlToProxy) {
        return proxy(serverId, urlToProxy, 0);
    }

    /**
     * creates a fake server and starts it on the specified port
     * @param serverId unique server id
     * @param port server port
     * @return server instance
     * @see WebtauRouter
     */
    public WebtauServer fake(String serverId, int port) {
        WebtauFakeRestServer server = new WebtauFakeRestServer(serverId, port);
        server.start();

        return server;
    }

    /**
     * creates a fake server and starts it on a random port
     * @param serverId unique server id
     * @return server instance
     * @see WebtauRouter
     */
    public WebtauServer fake(String serverId) {
        return fake(serverId, 0);
    }

    /**
     * creates a fake server and starts it on the specified port using provided router to defined responses
     * @param serverId unique server id
     * @param port server port
     * @param router responses definition
     * @return server instance
     * @see WebtauRouter
     */
    public WebtauServer fake(String serverId, int port, WebtauRouter router) {
        WebtauFakeRestServer server = new WebtauFakeRestServer(serverId, port, router);
        server.start();

        return server;
    }

    /**
     * creates a fake server and starts it on a random port using provided router to defined responses
     * @param serverId unique server id
     * @param router responses definition
     * @return server instance
     * @see WebtauRouter
     */
    public WebtauServer fake(String serverId, WebtauRouter router) {
        return fake(serverId, 0, router);
    }

    /**
     * creates an instance of router to define or override responses
     * @param id unique router id
     * @return router instance
     * @see WebtauRouter
     */
    public WebtauRouter router(String id) {
        return new WebtauRouter(id);
    }

    /**
     * creates an instance of router to define or override responses assigning default router id
     * @return router instance
     * @see WebtauRouter
     */
    public WebtauRouter router() {
        return new WebtauRouter("main-router");
    }
}
