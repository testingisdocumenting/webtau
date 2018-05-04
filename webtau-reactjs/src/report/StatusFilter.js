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

const SummaryEntry = ({number, label, selectedLabel, onTestStatusSelect}) => {
    const className = "test-summary" + (selectedLabel === label ? " selected" : "")
    return (
        <div className={className} onClick={() => onTestStatusSelect(label)}>
            <span className="entry total">{number + " " + label}</span>
        </div>
    )
}

const Navigation = ({summary, onTestStatusSelect, selectedLabel}) => {
    const labels = ["Total", "Passed", "Skipped", "Failed", "Errored"]
    return labels.map(l => <SummaryEntry key={l} label={l} number={summary[l.toLowerCase()]}
                                         selectedLabel={selectedLabel}
                                         onTestStatusSelect={onTestStatusSelect}/>)
}

const StatusFilter = ({summary, onTitleClick, onTestStatusSelect, selectedStatusFilter}) => {
    return (
        <div className="status-filter">
            <Navigation summary={summary}
                        onTestStatusSelect={onTestStatusSelect}
                        selectedLabel={selectedStatusFilter}/>
        </div>
    )
}

export default StatusFilter
