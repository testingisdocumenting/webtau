
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

import '../widgets/Table.css'
import './OperationsPerformanceTable.css'

export default function OperationsPerformanceTable({report}) {
    return (
        <table className="operations-performance table">
            <thead>
            <tr>
                <th>Method</th>
                <th>Url</th>
                <th>Count</th>
                <th>Fastest</th>
                <th>Slowest</th>
                <th>50 %</th>
                <th>75 %</th>
            </tr>
            </thead>
            <tbody>
            {report.performance.performancePerOperation.map(e => <PerformanceEntry key={e.key} entry={e}/>)}
            </tbody>
        </table>
    )
}

function PerformanceEntry({entry}) {
    return (
        <tr>
            <td>{entry.method}</td>
            <td>{entry.url}</td>
            <td>{entry.count}</td>
            <td>{entry.fastest}</td>
            <td>{entry.slowest}</td>
            <td>{entry.percentile[50].value}</td>
            <td>{entry.percentile[75].value}</td>
        </tr>
    )
}