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

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

class CliProcessConfig {
    public static final CliProcessConfig EMPTY = new CliProcessConfig();

    private Map<String, String> env;
    private File workingDir;

    public CliProcessConfig env(Map<String, CharSequence> env) {
        this.env = new HashMap<>();
        env.forEach((k, v) -> this.env.put(k, v.toString()));

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

    public Map<String, String> getEnv() {
        return env;
    }

    public File getWorkingDir() {
        return workingDir;
    }

    void applyTo(ProcessBuilder processBuilder) {
        if (env != null) {
            processBuilder.environment().putAll(env);
        }

        if (workingDir != null) {
            processBuilder.directory(workingDir);
        }
    }

    private File buildWorkingDir(String workingDir) {
        return buildWorkingDir(Paths.get(workingDir));
    }

    private File buildWorkingDir(Path workingDir) {
        if (workingDir.isAbsolute()) {
            return workingDir.toFile();
        }

        return WebTauConfig.getCfg().getWorkingDir()
                .resolve(workingDir)
                .normalize()
                .toAbsolutePath().toFile();
    }
}
