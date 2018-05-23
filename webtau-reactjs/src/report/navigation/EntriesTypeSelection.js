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

import './EntriesTypeSelection.css'
import NavigationEntriesType from './NavigationEntriesType'

function EntriesTypeSelection({selectedType, onSelect}) {
    return (
        <div className="entries-type-selection">
            <div className="webtau-link">
                <a href="https://github.com/twosigma/webtau" target="_blank">webtau</a>
            </div>
            <div className="selection-area">
                <EntryType selectedType={selectedType}
                           type={NavigationEntriesType.TESTS}
                           label="tests"
                           onSelect={onSelect}/>
                <EntryType selectedType={selectedType}
                           type={NavigationEntriesType.HTTP_CALLS}
                           label="http calls"
                           onSelect={onSelect}/>
            </div>
        </div>
    )
}

function EntryType({selectedType, type, label, onSelect}) {
    const isSelected = selectedType === type
    const className = 'entry-type entry-type-' + type + (isSelected ? ' selected' : '')

    return (
        <div className={className} onClick={() => onSelect(type)}>
            <div>{label}</div>
        </div>
    )
}

export default EntriesTypeSelection
