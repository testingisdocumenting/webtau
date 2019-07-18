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

import CardWithElapsedTime from './CardWithElapsedTime'
import CardWithTime from './CardWithTime'

export function cardWithElapsedTimeDemo(registry) {
    registry.add('with minutes', () => <CardWithElapsedTime millis={66321} label="Total time"/>)
    registry.add('with seconds', () => <CardWithElapsedTime millis={8321} label="Total time"/>)
    registry.add('with milliseconds', () => <CardWithElapsedTime millis={321} label="Total time"/>)
    registry.add('with decimals', () => <CardWithElapsedTime millis={1966321.23} label="Total time"/>)
    registry.add('with next card next to it', () => (
        <div style={{display: 'flex'}}>
            <CardWithTime millis={1547139662469} label="Start time"/>
            <CardWithElapsedTime millis={321} label="Total time"/>
        </div>
    ))
}
