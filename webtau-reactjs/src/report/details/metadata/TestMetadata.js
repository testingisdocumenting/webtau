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

import * as React from 'react'

import './TestMetadata.css'

export function TestMetadata({metadata}) {
    if (!metadata || Object.keys(metadata).length === 0) {
        return null
    }

    return (
        <div className="test-metadata">
            <table className="table">
                <thead>
                <tr>
                    <th>Metadata Key</th>
                    <th>Value</th>
                </tr>
                </thead>
                <tbody>
                {Object.keys(metadata).map(key => <MetadataEntry key={key} label={key} value={metadata[key]}/>)}
                </tbody>
            </table>
        </div>
    )
}

function MetadataEntry({label, value}) {
    return (
        <tr>
            <td>{label}</td>
            <td>{value}</td>
        </tr>
    )
}
