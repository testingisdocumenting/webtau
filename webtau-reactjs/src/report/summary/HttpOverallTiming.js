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

import CardWithTime from '../widgets/CardWithTime'
import CardWithElapsedTime from '../widgets/CardWithElapsedTime'

import CardList from '../widgets/CardList'

import './HttpOverallTiming.css'

export default function HttpOverallTiming({report}) {
    return (
        <CardList label="Tests run time">
            <CardWithTime label="Start Time (Local)"
                          time={report.summary.startTime}/>

            <CardWithTime label="Start Time (UTC)"
                          utc={true}
                          time={report.summary.startTime}/>
            <CardWithElapsedTime label="Overal Time"
                                 millis={report.summary.duration}/>
        </CardList>
    )
}