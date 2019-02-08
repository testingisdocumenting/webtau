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
import {scaleLinear} from 'd3-scale'

import './PercentilePerformanceDiagram.css'
import Card from '../widgets/Card'

const width = 800
const height = 600

export default class PercentilePerformanceDiagram extends React.Component {
    render() {
        const {report} = this.props

        const performance = report.performance
        const httpCalls = performance.sortedNotFailedHttpCalls

        if (httpCalls.length === 0) {
            return null
        }

        const percentile = performance.percentile[75]
        const latencyAxis = scaleLinear().domain([httpCalls[0].elapsedTime, percentile.value]).range([httpCalls[0].elapsedTime, height])
        const callIdxAxis = scaleLinear().domain([0, percentile.idx]).range([0, width])

        const reducedHttpCalls = httpCalls.slice(0, percentile.idx)
        return (
            <div>
                <Card width={width}>
                    <Diagram maxX={httpCalls.length} maxY={performance.maxLatency}>
                        {reducedHttpCalls.map((httpCall, idx) => <Point key={idx}
                                                                         x={callIdxAxis(idx)}
                                                                         y={latencyAxis(httpCall.elapsedTime)}/>)}
                    </Diagram>
                </Card>
            </div>
        )
    }
}

function Diagram({maxX, maxY, children}) {
    return (
        <svg width={width} height={height}>
            {children}
        </svg>
    )
}

function Point({x, y}) {
    return <rect width={5} height={5} x={x - 2} y={height - y - 2} stroke="#333" strokeWidth={1} fill="#888"/>
}