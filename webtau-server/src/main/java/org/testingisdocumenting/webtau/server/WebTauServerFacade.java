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
import org.testingisdocumenting.webtau.server.route.WebTauRouter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static org.testingisdocumenting.webtau.server.WebTauServerResponseBuilder.*;

public class WebTauServerFacade {
    public static final WebTauServerFacade server = new WebTauServerFacade();

    private WebTauServerFacade() {
    }

    public final WebTauServerResponseBuilder response = new WebTauServerResponseBuilder();

    /**
     * create response for a server based on the payload type. For String the response will be <code>plain/text</code> and for
     * map/list/beans it will be application/json.
     *
     * @see WebTauServerFacade#response
     * @see WebTauServerResponseBuilder#text
     * @param body body to serialize as response
     * @return response instance
     */
    public WebTauServerResponse response(Object body) {
        return response(USE_DEFAULT_STATUS_CODE, body);
    }

    /**
     * create response for a server based on the payload type with a specified status code.
     * For String the response will be <code>plain/text</code> and for
     * map/list/beans it will be application/json.
     *
     * @see WebTauServerFacade#response
     * @see WebTauServerResponseBuilder#text
     * @param statusCode status code to return
     * @param body body to serialize as response
     * @return response instance
     */
    public WebTauServerResponse response(int statusCode, Object body) {
        if (body instanceof String) {
            return response.text(statusCode, body.toString());
        }

        if (body == null) {
            return response.text(statusCode, "");
        }

        return response.json(statusCode, body);
    }

    /**
     * creates response for a server with empty response, empty content type and empty header
     * @param statusCode status code to return
     * @return response instance
     */
    public WebTauServerResponse statusCode(int statusCode) {
        return new WebTauServerResponse(statusCode, "", new byte[0], Collections.emptyMap());
    }

    /**
     * creates static content server and starts it on a random part
     * @param serverId unique server id
     * @param path static content path
     * @return server instance
     */
    public WebTauServer serve(String serverId, String path) {
        return serve(serverId, path, 0);
    }

    /**
     * creates static content server and starts it on a random part
     * @param serverId unique server id
     * @param path static content path
     * @return server instance
     */
    public WebTauServer serve(String serverId, Path path) {
        return serve(serverId, path, 0);
    }

    /**
     * creates static content server and starts it on the specified part
     * @param serverId unique server id
     * @param path static content path
     * @param port server port
     * @return server instance
     */
    public WebTauServer serve(String serverId, String path, int port) {
        return serve(serverId, Paths.get(path), port);
    }

    /**
     * creates static content server and starts it on the specified part
     * @param serverId unique server id
     * @param path static content path
     * @param port server port
     * @return server instance
     */
    public WebTauServer serve(String serverId, Path path, int port) {
        WebTauStaticServer server = new WebTauStaticServer(serverId, WebTauConfig.getCfg().fullPath(path), port);
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
    public WebTauProxyServer proxy(String serverId, String urlToProxy, int port) {
        WebTauProxyServer server = new WebTauProxyServer(serverId, urlToProxy, port);
        server.start();

        return server;
    }

    /**
     * creates proxy server and starts it on a random part
     * @param serverId unique server id
     * @param urlToProxy url to proxy to
     * @return server instance
     */
    public WebTauProxyServer proxy(String serverId, String urlToProxy) {
        return proxy(serverId, urlToProxy, 0);
    }

    /**
     * creates a fake server and starts it on the specified port
     * @param serverId unique server id
     * @param port server port
     * @return server instance
     * @see WebTauRouter
     */
    public WebTauServer fake(String serverId, int port) {
        WebTauFakeRestServer server = new WebTauFakeRestServer(serverId, port);
        server.start();

        return server;
    }

    /**
     * creates a fake server and starts it on a random port
     * @param serverId unique server id
     * @return server instance
     * @see WebTauRouter
     */
    public WebTauServer fake(String serverId) {
        return fake(serverId, 0);
    }

    /**
     * creates a fake server and starts it on the specified port using provided router to defined responses
     * @param serverId unique server id
     * @param port server port
     * @param router responses definition
     * @return server instance
     * @see WebTauRouter
     */
    public WebTauServer fake(String serverId, int port, WebTauRouter router) {
        WebTauFakeRestServer server = new WebTauFakeRestServer(serverId, port, router);
        server.start();

        return server;
    }

    /**
     * creates a fake server and starts it on a random port using provided router to defined responses
     * @param serverId unique server id
     * @param router responses definition
     * @return server instance
     * @see WebTauRouter
     */
    public WebTauServer fake(String serverId, WebTauRouter router) {
        return fake(serverId, 0, router);
    }

    /**
     * creates an instance of router to define or override responses
     * @param id unique router id
     * @return router instance
     * @see WebTauRouter
     */
    public WebTauRouter router(String id) {
        return new WebTauRouter(id);
    }

    /**
     * creates an instance of router to define or override responses assigning default router id
     * @return router instance
     * @see WebTauRouter
     */
    public WebTauRouter router() {
        return new WebTauRouter("main-router");
    }
}
