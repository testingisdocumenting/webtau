/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.cfg

class WebTauGroovyCliArgsConfigHandler implements WebTauConfigHandler {
    private String[] args
    private WebTauCliArgsConfig argsConfig

    WebTauGroovyCliArgsConfigHandler(String[] args) {
        this.args = args
    }

    @Override
    void onBeforeCreate(WebTauConfig cfg) {
    }

    @Override
    void onAfterCreate(WebTauConfig cfg) {
        println "todo debug: $cfg"
        /*
         * this config handler is registered twice. before any other handler, and then after all handlers.
         * We need to parse args first to get config file essential params first.
         * After it is done and config file handler is ran, we need to be able to override config values using args.
         * 1. get config related params
         * 2. read config file if present
         * 3. set other arg values on top as overrides
         */
        if (! argsConfig) {
            argsConfig = new WebTauCliArgsConfig(cfg, args)
            argsConfig.setConfigFileRelatedCfgIfPresent()
        } else {
            argsConfig.setRestOfConfigValuesFromArgs()
        }
    }

    List<String> getTestFiles() {
        return argsConfig.testFiles
    }
}
