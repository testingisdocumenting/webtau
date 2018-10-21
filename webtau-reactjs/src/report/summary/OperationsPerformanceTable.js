
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

import SortableTable from '../widgets/SortableTable'

import './OperationsPerformanceTable.css'

export default function OperationsPerformanceTable({report}) {
    return (
        <SortableTable className="operations-performance" header={header()} data={prepareData(report)}/>
    )
}

function header() {
    return [
        'Method',
        'Url',
        'Count',
        'Fastest',
        'Slowest',
        '50 %',
        '75 %'
    ]
}

function prepareData(report) {
    return report.performance.performancePerOperation.map(e => [
        e.method,
        e.url,
        e.count,
        e.fastest,
        e.slowest,
        e.percentile[50].value,
        e.percentile[75].value])
}
