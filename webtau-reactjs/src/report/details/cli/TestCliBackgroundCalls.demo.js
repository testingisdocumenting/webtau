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

import React from 'react'

import {simulateState} from "react-component-viewer"
import TestCliBackground from './TestCliBackground'

const [getUrlState, setUrlState] = simulateState({cliBackgroundIdxs: "0-1-2"})

export function testCliBackgroundCallsDemo(registry) {
    registry.add('multiple calls', () => (
        <TestCliBackground test={createTestWithCliBackground()} urlState={getUrlState()}
                           onInternalStateUpdate={setUrlState}/>
    ))
}

function createTestWithCliBackground() {
    return {
        cliBackground: [
            {
                command: "ls -l",
                out: 'line 1\nline 2\nline 3',
                err: 'line 4\nline 5',
                startTime: 0,
            },
            {
                command: "ls2 -l",
                out: 'line 1\nline 2\nline 3',
                err: 'line 4\nline 5',
                startTime: 165210,
            },
        ]
    }
}