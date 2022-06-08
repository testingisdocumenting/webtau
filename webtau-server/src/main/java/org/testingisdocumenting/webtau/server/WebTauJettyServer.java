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

import org.eclipse.jetty.server.*;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.reporter.WebTauStepOutput;
import org.testingisdocumenting.webtau.server.registry.WebTauServerJournal;
import org.testingisdocumenting.webtau.server.registry.WebTauServerJournalJettyHandler;
import org.testingisdocumenting.webtau.server.registry.WebTauServersRegistry;
import org.testingisdocumenting.webtau.utils.UrlUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.*;
import static org.testingisdocumenting.webtau.reporter.WebTauStepInputKeyValue.*;
import static org.testingisdocumenting.webtau.reporter.WebTauStepOutputKeyValue.*;
import static org.testingisdocumenting.webtau.server.registry.WebTauServersRegistry.*;

/**
 * base for defining jetty based servers
 * handles start/stop and report steps
 */
abstract public class WebTauJettyServer implements WebTauServer {
    protected final String serverId;
    protected final int passedPort;
    protected final WebTauServerJournal journal;

    protected Server server;
    protected boolean isStarted;
    protected boolean isRunning;

    public WebTauJettyServer(String id, int passedPort) {
        this.serverId = id;
        this.passedPort = passedPort;
        this.journal = new WebTauServerJournal(id);
    }

    @Override
    public WebTauServerJournal getJournal() {
        return journal;
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
        return isRunning;
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
        WebTauServer.super.setAsBaseUrl();
    }

    @Override
    public void addOverride(WebTauServerOverride override) {
        WebTauServerGlobalOverrides.addContentOverride(serverId, override);
    }

    @Override
    public void markUnresponsive() {
        WebTauServerGlobalOverrides.addStateOverride(serverId, new WebTauServerOverrideNoResponse(serverId));
    }

    @Override
    public void markBroken() {
        WebTauServerGlobalOverrides.addStateOverride(serverId, new WebTauServerOverrideFailedResponse());
    }

    @Override
    public void fix() {
        WebTauServerGlobalOverrides.removeStateOverride(serverId, WebTauServerOverrideNoResponse.OVERRIDE_ID);
        WebTauServerGlobalOverrides.removeStateOverride(serverId, WebTauServerOverrideFailedResponse.OVERRIDE_ID);
        ServerResponseWaitLocks.releaseLock(serverId);
    }

    @Override
    public void removeOverride(String overrideId) {
        WebTauServerGlobalOverrides.removeOverride(serverId, overrideId);
    }

    abstract protected Map<String, Object> provideStepInput();
    abstract protected void validateParams();

    abstract protected Handler createJettyHandler();

    private void startStep() {
        validateId(serverId);
        validateParams();
        server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(passedPort);
        connector.setHost("127.0.0.1");
        server.addConnector(connector);

        Handler jettyHandler = createJettyHandler();
        server.setHandler(autoAddToJournal() ?
                new WebTauServerJournalJettyHandler(journal, jettyHandler):
                jettyHandler);

        try {
            server.start();
            WebTauServersRegistry.register(this);
            isStarted = true;
            isRunning = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void stopStep() {
        try {
            ServerResponseWaitLocks.releaseLock(serverId);
            server.stop();
            isRunning = false;
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
