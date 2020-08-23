/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.cfg;

import org.testingisdocumenting.webtau.documentation.DocumentationArtifactsLocation;
import org.testingisdocumenting.webtau.expectation.timer.SystemTimerConfig;

import java.nio.file.Path;

/**
 * <p>
 * core module doesn't depend on config so it can be used independently.
 * instead of using config to driver values of core modules you set values directly on certain components
 * e.g
 * {@link org.testingisdocumenting.webtau.expectation.timer.SystemTimerConfig#setWaitTimeout(int)}
 * {@link org.testingisdocumenting.webtau.documentation.DocumentationArtifactsLocation#setRoot(Path)}
 * <p>
 * this config handler is going to be run last and have access to config values.
 * It will set config values of the core components using config values provided by config handlers (e.g. config files)
 */
public class WebTauCoreConfigHandler implements WebTauConfigHandler {
    @Override
    public void onAfterCreate(WebTauConfig cfg) {
        SystemTimerConfig.setWaitTimeout(cfg.waitTimeout());
        DocumentationArtifactsLocation.setRoot(cfg.getDocArtifactsPath());
    }
}
