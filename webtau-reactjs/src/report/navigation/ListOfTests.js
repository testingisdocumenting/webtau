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

import NavigationEntry from './NavigationEntry'
import NavigationEntryGroupLabel from './NavigationEntryGroupLabel'

import './ListOfTests.css'

function ListOfTests({testGroups, onTestSelect, onTestGroupSelect, selectedId}) {
    return (
        <div className="list-of-tests">
            {testGroups.map((group) => <TestsGroup key={group.id}
                                                   tests={group.tests}
                                                   selectedId={selectedId}
                                                   onTestSelect={onTestSelect}
                                                   onTestGroupSelect={onTestGroupSelect}/>)}
        </div>
    )
}

function TestsGroup({tests, onTestSelect, onTestGroupSelect, selectedId}) {
    const renderedTests = tests.map((test) => <TestEntry key={test.id}
                                                         test={test}
                                                         onSelect={onTestSelect}
                                                         isSelected={test.id === selectedId}/>)
    const groupLabel = tests[0].shortContainerName

    return (
        <div className="group-of-tests">
            <NavigationEntryGroupLabel label={groupLabel} onSelect={() => onTestGroupSelect(groupLabel)}/>

            {renderedTests}
        </div>
    )
}

function TestEntry({test, onSelect, isSelected}) {
    return (
        <NavigationEntry label={test.scenario}
                         status={test.status}
                         isSelected={isSelected}
                         onSelect={() => onSelect(test.id)}/>
    )
}

export default ListOfTests
