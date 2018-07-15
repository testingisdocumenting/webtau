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
import './ConfigTable.css'

export default function ConfigTable({report}) {
    return (
        <table className="config-table table">
            <thead>
            <tr>
                <th>Key</th>
                <th>Value</th>
                <th>Source</th>
            </tr>
            </thead>
            <tbody>
            {report.config.map(e => <ReportEntry key={e.key} entry={e}/>)}
            </tbody>
        </table>
    )
}

function ReportEntry({entry}) {
    return (
        <tr>
            <td>{entry.key}</td>
            <td>{entry.value}</td>
            <td>{entry.source}</td>
        </tr>
    )
}