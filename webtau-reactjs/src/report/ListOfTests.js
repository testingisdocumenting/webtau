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
