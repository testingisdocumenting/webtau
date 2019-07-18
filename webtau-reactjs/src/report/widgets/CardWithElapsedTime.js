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

import Card from './Card'

import ElapsedTimeFragment from './ElapsedTimeFragment'

import './CardWithElapsedTime.css'

function CardWithElapsedTime({millis, label}) {
    const totalSeconds = (millis / 1000) | 0
    const minutes = totalSeconds / 60 | 0
    const seconds = totalSeconds % 60
    const remainingMs = (millis % 1000) | 0

    return (
        <Card className="card-with-elapsed-time">
            <div className="card-time-part">
                <ElapsedTimeFragment value={minutes} label="min"/>
                <ElapsedTimeFragment value={seconds} label="s"/>
                <ElapsedTimeFragment value={remainingMs} label="ms"/>
            </div>
            <div className="card-label">{label}</div>
        </Card>
    )
}

export default CardWithElapsedTime
