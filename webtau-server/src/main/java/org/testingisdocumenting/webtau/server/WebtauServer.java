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

public interface WebtauServer extends AutoCloseable {
    String getId();
    String getType();
    int getPort();
    String getBaseUrl();
    boolean isRunning();

    void start();
    void stop();

    void markUnresponsive();
    void markResponsive();

    void markBroken();

    void addOverride(WebtauServerOverride override);
    void removeOverride(String overrideId);

    default void setAsBaseUrl() {
        WebTauConfig.getCfg().setBaseUrl("server-" + getId(), getBaseUrl());
    }

    @Override
    default void close() {
        stop();
    }
}
