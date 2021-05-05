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

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder;
import org.testingisdocumenting.webtau.reporter.WebTauStep;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.*;

class StaticContentServer implements WebtauServer {
    private final String id;
    private final Path path;
    private final int passedPort;
    private Server server;

    public StaticContentServer(String id, Path path, int port) {
        this.id = id;
        this.path = path;
        this.passedPort = port;
    }

    public void start() {
        WebTauStep.createAndExecuteStep(
                tokenizedMessage(action("starting"), classifier("static server"),
                        IntegrationTestsMessageBuilder.id(id), COLON, urlValue(path)),
                () -> tokenizedMessage(action("started"), classifier("static server"), ON, numberValue(getPort())),
                this::startStep);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return "static server";
    }

    @Override
    public void stop() {
        WebTauStep.createAndExecuteStep(
                tokenizedMessage(action("stopping"), classifier("server"), id(id)),
                () -> tokenizedMessage(action("stopped"), classifier("server"), id(id)),
                this::stopStep);
    }

    @Override
    public int getPort() {
        return server.getURI().getPort();
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    private void validatePath() {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("can't find path: " + path);
        }
    }

    private void startStep() {
        validatePath();
        server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(passedPort);
        connector.setHost("127.0.0.1");
        server.addConnector(connector);

        ResourceHandler handler = new ResourceHandler();
        handler.setBaseResource(Resource.newResource(path));

        server.setHandler(handler);

        try {
            server.start();
            WebtauServersRegistry.register(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void stopStep() {
        try {
            server.stop();
            WebtauServersRegistry.unregister(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
