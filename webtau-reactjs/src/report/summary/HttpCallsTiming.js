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

import React from 'react'

import NumberOfHttpCalls from '../dashboard/NumberOfHttpCalls'
import CardList from '../widgets/CardList'
import CardWithElapsedTime from '../widgets/CardWithElapsedTime'

export default function HttpCallsTiming({report, onSwitchToHttpCalls}) {
    return (
        <CardList label="HTTP calls time">
            <div className="overall-number-of-http-calls" onClick={onSwitchToHttpCalls}>
                <NumberOfHttpCalls number={report.numberOfHttpCalls()}/>
            </div>

            <CardWithElapsedTime label="Average Time"
                                 millis={report.averageHttpCallTime().toFixed(2)}/>

            <CardWithElapsedTime label="Overall Time"
                                 millis={report.overallHttpCallTime()}/>
        </CardList>
    )
}
