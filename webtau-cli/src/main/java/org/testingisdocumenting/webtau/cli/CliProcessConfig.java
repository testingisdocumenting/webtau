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
 *
 */

package org.testingisdocumenting.webtau.cli;

import org.testingisdocumenting.webtau.cfg.WebTauConfig;
import org.testingisdocumenting.webtau.persona.Persona;
import org.testingisdocumenting.webtau.reporter.WebTauStepInput;
import org.testingisdocumenting.webtau.reporter.WebTauStepInputKeyValue;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

class CliProcessConfig {
    public static final CliProcessConfig SILENT = new CliProcessConfig().silent();

    private final String personaId;
    private final Map<String, String> env;
    private File workingDir;
    private boolean isSilent;

    private long timeoutMs;
    private boolean timeoutSpecified;

    public static CliProcessConfig createEmpty() {
        return new CliProcessConfig();
    }

    CliProcessConfig() {
        this.personaId = Persona.getCurrentPersona().getId();
        this.env = new LinkedHashMap<>();
        CliConfig.getCliEnv().forEach((k, v) -> env.put(k, v.toString()));
    }

    public CliProcessConfig env(Map<String, Object> env) {
        env.forEach((k, v) -> this.env.put(k, v.toString()));

        return this;
    }

    public CliProcessConfig timeout(long millis) {
        this.timeoutMs = millis;
        this.timeoutSpecified = true;
        return this;
    }

    public CliProcessConfig workingDir(String workingDir) {
        this.workingDir = buildWorkingDir(workingDir);
        return this;
    }

    public CliProcessConfig workingDir(Path workingDir) {
        this.workingDir = buildWorkingDir(workingDir);
        return this;
    }

    public CliProcessConfig silent() {
        this.isSilent = true;
        return this;
    }

    public Map<String, String> getEnv() {
        return env;
    }

    public File getWorkingDir() {
        return workingDir;
    }

    public boolean isSilent() {
        return isSilent;
    }

    public long getTimeoutMs() {
        return timeoutMs;
    }

    public boolean isTimeoutSpecified() {
        return timeoutSpecified;
    }

    public String getPersonaId() {
        return personaId;
    }

    WebTauStepInput createStepInput() {
        Map<String, Object> input = new LinkedHashMap<>();

        if (workingDir != null) {
            input.put("working dir", workingDir.toString());
        }

        if (timeoutSpecified) {
            input.put("local timeout", timeoutMs);
        }

        env.forEach((k, v) -> input.put("$" + k, v));

        return WebTauStepInputKeyValue.stepInput(input);
    }

    void applyTo(ProcessBuilder processBuilder) {
        if (!env.isEmpty()) {
            processBuilder.environment().putAll(env);
        }

        if (workingDir != null) {
            processBuilder.directory(workingDir);
        } else {
            processBuilder.directory(WebTauConfig.getCfg().getWorkingDir().toFile());
        }
    }

    private File buildWorkingDir(String workingDir) {
        return buildWorkingDir(Paths.get(workingDir));
    }

    private File buildWorkingDir(Path workingDir) {
        if (workingDir.isAbsolute()) {
            return workingDir.toFile();
        }

        return WebTauConfig.getCfg().fullPath(workingDir)
                .normalize()
                .toAbsolutePath().toFile();
    }
}
