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

import './StatusFilter.css'

function StatusFilter({summary, onTitleClick, onStatusSelect, selectedStatusFilter}) {
    const labels = ["Total", "Passed", "Skipped", "Failed", "Errored"]
    return (
        <div className="status-filter">
            {labels.map(l => <SummaryEntry key={l} label={l} number={summary[l.toLowerCase()]}
                                           selectedLabel={selectedStatusFilter}
                                           onStatusSelect={onStatusSelect}/>)}
        </div>
    )
}

function SummaryEntry({number, label, selectedLabel, onStatusSelect}) {
    const isSelected = (selectedLabel === label) || (label === 'Total' && !selectedLabel)
    const className = "test-summary" + (isSelected ? " selected" : "")
    return (
        <div className={className} onClick={() => onStatusSelect(label)}>
            <span>{number + " " + label}</span>
        </div>
    )
}

export default StatusFilter
