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

import './ListOfTests.css'

const Status = ({status}) => {
    const className = "status " + status.toLowerCase()
    return (
        <div className={className}>{mark()}</div>
    )

    function mark() {
        switch (status) {
            case "Failed":
                return "\u2715"
            case "Errored":
                return "\u007e"
            case "Passed":
                return "\u2714"
            case "Skipped":
                return "\u25cb"
            default:
                return "Unknown"
        }
    }
}

const TestCard = ({test, onSelect, isSelected}) => {
    const className = "test-card" + (isSelected ? " selected" : "")
    return (
        <div className={className} onClick={() => onSelect(test.id)}>
            <div className="scenario">{test.scenario}</div>
            <Status status={test.status}/>
        </div>
    )
}

const ListOfTests = ({tests, onSelect, selectedId}) => {
    return (
        <div className="list-of-tests">
            {tests.map((test) => <TestCard key={test.id} test={test} onSelect={onSelect} isSelected={test.id === selectedId}/>)}
        </div>
    )
}

export default ListOfTests
