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
import org.testingisdocumenting.webtau.server.journal.WebtauServerJournal;
import org.testingisdocumenting.webtau.server.journal.WebtauServerJournalJettyHandler;
import org.testingisdocumenting.webtau.utils.UrlUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.*;
import static org.testingisdocumenting.webtau.reporter.WebTauStepInputKeyValue.*;
import static org.testingisdocumenting.webtau.reporter.WebTauStepOutputKeyValue.*;
import static org.testingisdocumenting.webtau.server.WebtauServersRegistry.*;

/**
 * base for defining jetty based servers
 * handles start/stop and report steps
 */
abstract public class WebtauJettyServer implements WebtauServer {
    protected final String serverId;
    protected final int passedPort;
    protected final WebtauServerJournal journal;

    protected Server server;
    protected boolean started;

    public WebtauJettyServer(String id, int passedPort) {
        this.serverId = id;
        this.passedPort = passedPort;
        this.journal = new WebtauServerJournal(id);
    }

    @Override
    public WebtauServerJournal getJournal() {
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
    public void addOverride(WebtauServerOverride override) {
        WebtauServerGlobalOverrides.addContentOverride(serverId, override);
    }

    @Override
    public void markUnresponsive() {
        WebtauServerGlobalOverrides.addStateOverride(serverId, new WebtauServerOverrideNoResponse(serverId));
    }

    @Override
    public void markBroken() {
        WebtauServerGlobalOverrides.addStateOverride(serverId, new WebtauServerOverrideFailedResponse());
    }

    @Override
    public void fix() {
        WebtauServerGlobalOverrides.removeStateOverride(serverId, WebtauServerOverrideNoResponse.OVERRIDE_ID);
        WebtauServerGlobalOverrides.removeStateOverride(serverId, WebtauServerOverrideFailedResponse.OVERRIDE_ID);
        ServerResponseWaitLocks.releaseLock(serverId);
    }

    @Override
    public void removeOverride(String overrideId) {
        WebtauServerGlobalOverrides.removeOverride(serverId, overrideId);
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
        server.setHandler(new WebtauServerJournalJettyHandler(journal, jettyHandler));

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
