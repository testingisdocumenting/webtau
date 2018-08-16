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
import moment from 'moment'

import Card from './Card'
import './CardWithTime.css'

function CardWithTime({label, time, utc}) {
    const local = utc ?
        moment(time).utc():
        moment(time).local()

    return (
        <Card className="card-with-time">
            <div className="card-date">{local.format('MMM DD ddd')}</div>
            <div className="card-time">{local.format('HH:MM.ms')}</div>
            <div className="card-label">{label}</div>
        </Card>
    )
}

export default CardWithTime
