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

import './ElapsedTime.css'

function ElapsedTime({millis}) {
    const seconds = (millis / 1000) | 0
    const remainingMs = millis % 1000

    return (
        <React.Fragment>
            <Seconds seconds={seconds}/>
            <Millis millis={remainingMs}/>
        </React.Fragment>
    )
}

function Seconds({seconds}) {
    if (seconds === 0) {
        return null
    }

    return (
        <React.Fragment>
            <span className="elapsed-seconds">{seconds}</span>
            <span className="time-unit">s</span>
        </React.Fragment>
    )
}

function Millis({millis}) {
    if (millis === 0) {
        return null
    }

    return (
        <React.Fragment>
            <span className="elapsed-millis">{millis}</span>
            <span className="time-unit">ms</span>
        </React.Fragment>
    )
}

export default ElapsedTime
