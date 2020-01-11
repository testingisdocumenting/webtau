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

import * as React from 'react'

import {TestMeta} from './TestMeta'

export function testMetaDemo(registry) {
    registry
        .add('no values', () => <TestMeta meta={{}}/>)
        .add('undefined value', () => <TestMeta meta={undefined}/>)
        .add('with values', () => <TestMeta meta={{owner: 'Team A', severity: 100}}/>)
}
