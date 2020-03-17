/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.cli.interactive

import groovy.transform.PackageScope

@PackageScope
enum InteractiveCommand {
    Back(['back', 'b'], 'go back'),
    Run(['run', 'r'], 'run selected scenario(s)'),
    Watch(['watch', 'w'], 'run selected scenario(s)'),
    StopWatch(['stop', 's'], 'stop watching selected scenario(s)'),
    Quit(['quit', 'q'], 'stop interactive mode')

    private List<String> prefixes
    private String description

    InteractiveCommand(List<String> prefixes, String description) {
        this.prefixes = prefixes
        this.description = description
    }

    boolean matches(String lowerCaseText) {
        return prefixes.find { it == lowerCaseText }
    }

    List<String> getPrefixes() {
        return prefixes
    }

    String getDescription() {
        return description
    }
}
