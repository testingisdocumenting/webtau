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

import React from 'react'

import CliOutputCard from "./CliOutputCard"
import CliCommandCard from "./CliCommandCard"

export function cliBuildingBlocksDemo(registry) {
    registry
        .add('command', () => (
            <CliCommandCard command="my/script --param=name"/>
        ))
        .add('output with matches', () => (
            <CliOutputCard output={generateOutput(10)} matchedLines={['output line #2', 'output line #8']}/>
        ))
       .add('output with scroll', () => (
            <CliOutputCard output={generateOutput(40)} matchedLines={['output line #2', 'output line #17']}/>
        ))
}

function generateOutput(numberOfLines) {
    const lines = [];
    for (let i = 0; i < numberOfLines; i++) {
        const prefix = i % 3 === 0 ? '    ' : '';
        lines.push(prefix + 'output line #' + i)
    }

    return lines.join('\n')
}