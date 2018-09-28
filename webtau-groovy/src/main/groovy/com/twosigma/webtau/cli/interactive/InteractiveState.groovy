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

package com.twosigma.webtau.cli.interactive

import groovy.transform.PackageScope

@PackageScope
enum InteractiveState {
    TestSelection([InteractiveCommand.Quit]),
    ScenarioSelection([InteractiveCommand.Back, InteractiveCommand.Quit]),
    RunSelectedScenario([InteractiveCommand.Quit]),
    WatchingSelectedScenario([InteractiveCommand.Run, InteractiveCommand.StopWatch,
                              InteractiveCommand.Back, InteractiveCommand.Quit]),
    AfterScenarioRun([InteractiveCommand.Run, InteractiveCommand.Watch,
                      InteractiveCommand.Back, InteractiveCommand.Quit]),
    Done([])

    private List<InteractiveCommand> availableCommands

    InteractiveState(List<InteractiveCommand> availableCommands) {
        this.availableCommands = availableCommands
    }

    List<InteractiveCommand> getAvailableCommands() {
        return availableCommands
    }
}
