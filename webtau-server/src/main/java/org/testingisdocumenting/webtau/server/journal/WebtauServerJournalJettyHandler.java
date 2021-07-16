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

package org.testingisdocumenting.webtau.server.journal;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.annotation.ManagedAttribute;
import org.eclipse.jetty.util.annotation.ManagedOperation;
import org.testingisdocumenting.webtau.time.Time;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebtauServerJournalJettyHandler implements Handler {
    private final WebtauServerJournal journal;
    private final Handler delegate;

    public WebtauServerJournalJettyHandler(WebtauServerJournal journal, Handler delegate) {
        this.journal = journal;
        this.delegate = delegate;
    }

    @Override
    public void handle(String uri, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        long startTime = Time.currentTimeMillis();
        delegate.handle(uri, baseRequest, request, response);
        long endTime = Time.currentTimeMillis();

        journal.registerCall(request.getMethod(), request.getRequestURI(), request.getContentType(),
                startTime, (endTime - startTime));
    }

    @Override
    public void setServer(Server server) {
        delegate.setServer(server);
    }

    @Override
    @ManagedAttribute(value = "the jetty server for this handler", readonly = true)
    public Server getServer() {
        return delegate.getServer();
    }

    @Override
    @ManagedOperation(value = "destroy associated resources", impact = "ACTION")
    public void destroy() {
        delegate.destroy();
    }

    @Override
    @ManagedOperation(value = "Starts the instance", impact = "ACTION")
    public void start() throws Exception {
        delegate.start();
    }

    @Override
    @ManagedOperation(value = "Stops the instance", impact = "ACTION")
    public void stop() throws Exception {
        delegate.stop();
    }

    @Override
    public boolean isRunning() {
        return delegate.isRunning();
    }

    @Override
    public boolean isStarted() {
        return delegate.isStarted();
    }

    @Override
    public boolean isStarting() {
        return delegate.isStarting();
    }

    @Override
    public boolean isStopping() {
        return delegate.isStopping();
    }

    @Override
    public boolean isStopped() {
        return delegate.isStopped();
    }

    @Override
    public boolean isFailed() {
        return delegate.isFailed();
    }

    @Override
    public void addLifeCycleListener(Listener listener) {
        delegate.addLifeCycleListener(listener);
    }

    @Override
    public void removeLifeCycleListener(Listener listener) {
        delegate.removeLifeCycleListener(listener);
    }
}
