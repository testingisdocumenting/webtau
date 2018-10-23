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

import './HttpHeader.css'

export default function HttpHeader({header}) {
    return (
        <div className="http-header">
            {header.map((entry, idx) => (
                <React.Fragment key={idx}>
                    <div className="http-header-key http-header-entry">{entry.key}</div>
                    <div className="http-header-value http-header-entry">{entry.value}</div>
                </React.Fragment>
            ))}
        </div>
    )
}