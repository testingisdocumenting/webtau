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
import TestErrorMessage from './TestErrorMessage'

export function testErrorMessageDemo(registry) {
    registry.add('short line', () => <TestErrorMessage message={shortLineMessage()}/>)
    registry.add('long line', () => <TestErrorMessage message={longLineMessage(8)}/>)
    registry.add('many lines', () => <TestErrorMessage message={longLineMessage(100)}/>)
}

function shortLineMessage() {
    return 'exception at\nhello world'
}

function longLineMessage(numberOfLines) {
    const part = 'exception at hello world long line long line and a bit longer yet'
    const longLine = Array(8).fill().map(_ => part).join(' ')
    return Array(numberOfLines).fill().map(_ => longLine).join('\n')
}
