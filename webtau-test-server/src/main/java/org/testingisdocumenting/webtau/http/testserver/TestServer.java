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

package org.testingisdocumenting.webtau.http.testserver;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;

import java.net.URI;
import java.net.URISyntaxException;

public class TestServer {
    private Server server;
    private final Handler handler;

    public TestServer(Handler handler) {
        this.handler = handler;
    }

    public void startRandomPort() {
        start(0);
    }

    public void start(int port) {
        server = new Server(port);

        GzipHandler gzipHandler = new GzipHandler();
        gzipHandler.setHandler(handler);

        server.setHandler(gzipHandler);
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public URI getUri() {
        try {
            // forcing localhost as I seen different IP being listed when ran on github actions
            return new URI("http://localhost:" + server.getURI().getPort());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
