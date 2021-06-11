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
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.reporter.WebTauStepOutput;
import org.testingisdocumenting.webtau.utils.UrlUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.*;
import static org.testingisdocumenting.webtau.reporter.WebTauStepInputKeyValue.*;
import static org.testingisdocumenting.webtau.reporter.WebTauStepOutputKeyValue.*;
import static org.testingisdocumenting.webtau.server.WebtauServersRegistry.*;

abstract public class JettyServer implements WebtauServer {
    protected final String serverId;
    protected final int passedPort;
    protected Server server;
    protected boolean started;

    public JettyServer(String id, int passedPort) {
        this.serverId = id;
        this.passedPort = passedPort;
    }

    @Override
    public String getId() {
        return serverId;
    }

    @Override
    public int getPort() {
        validateStarted();
        return server.getURI().getPort();
    }

    @Override
    public String getBaseUrl() {
        validateStarted();
        return UrlUtils.removeTrailingSlash(server.getURI().toASCIIString());
    }

    @Override
    public boolean isRunning() {
        return started;
    }

    @Override
    public void start() {
        Map<String, Object> input = new LinkedHashMap<>(provideStepInput());
        input.put("passed port", passedPort == 0 ? "random" : passedPort);

        WebTauStep.createAndExecuteStep(
                tokenizedMessage(action("starting"), classifier(getType()), id(serverId)),
                stepInput(input),
                () -> tokenizedMessage(action("started"), classifier(getType()), id(serverId)),
                () -> isRunning() ? stepOutput("running port", getPort()) : WebTauStepOutput.EMPTY,
                this::startStep);
    }

    @Override
    public void stop() {
        WebTauStep.createAndExecuteStep(
                tokenizedMessage(action("stopping"), classifier("server"), id(serverId)),
                () -> tokenizedMessage(action("stopped"), classifier("server"), id(serverId)),
                this::stopStep);
    }

    @Override
    public void setAsBaseUrl() {
        WebtauServer.super.setAsBaseUrl();
    }

    @Override
    public void markUnresponsive() {
        throw new UnsupportedOperationException("markUnreachable is not implemented for type: " + getType());
    }

    @Override
    public void markResponsive() {
        throw new UnsupportedOperationException("markReachable is not implemented for type: " + getType());
    }

    @Override
    public void markBroken() {
        throw new UnsupportedOperationException("markBroken is not implemented for type: " + getType());
    }

    @Override
    public void addOverride(String overrideId, WebtauServerOverride override) {
        throw new UnsupportedOperationException("addOverride is not implemented for type: " + getType());
    }

    @Override
    public void removeOverride(String overrideId) {
        throw new UnsupportedOperationException("removeOverride is not implemented for type: " + getType());
    }

    abstract protected Map<String, Object> provideStepInput();
    abstract protected void validateParams();

    abstract protected HandlerWrapper createJettyHandler();

    private void startStep() {
        validateId(serverId);
        validateParams();
        server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(passedPort);
        connector.setHost("127.0.0.1");
        server.addConnector(connector);

        server.setHandler(createJettyHandler());

        try {
            server.start();
            WebtauServersRegistry.register(this);
            started = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void stopStep() {
        try {
            ServerResponseWaitLocks.releaseLock(serverId);
            server.stop();
            WebtauServersRegistry.unregister(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void validateStarted() {
        if (!isRunning()) {
            throw new IllegalStateException("server is not started");
        }
    }
}
