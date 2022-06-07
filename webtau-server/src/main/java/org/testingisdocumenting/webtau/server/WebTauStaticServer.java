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
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

class WebTauStaticServer extends WebTauJettyServer {
    private final Path path;

    public WebTauStaticServer(String id, Path path, int port) {
        super(id, port);
        this.path = path;
    }

    @Override
    protected Map<String, Object> provideStepInput() {
        return Collections.singletonMap("path", path);
    }

    @Override
    protected void validateParams() {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("can't find path: " + path);
        }
    }

    @Override
    protected Handler createJettyHandler() {
        ResourceHandler handler = new WebTauServerStaticJettyHandler(serverId);
        try {
            handler.setBaseResource(Resource.newResource(path.toRealPath()));
            return handler;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String getType() {
        return "static server";
    }
}
