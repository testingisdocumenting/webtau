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
