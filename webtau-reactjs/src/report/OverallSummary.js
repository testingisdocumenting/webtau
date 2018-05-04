import React from 'react'

import NumberOfHttpCalls from './dashboard/NumberOfHttpCalls'
import CardLabelAndNumber from './widgets/CardLabelAndNumber'

import './OverallSummary.css'

const OverallSummary = ({report}) => {
    return (
        <div className="overall-summary">
            <NumberOfHttpCalls number={report.numberOfHttpCalls()}/>

            <CardLabelAndNumber label="Average Time (ms)"
                                number={report.averageHttpCallTime().toFixed(2)}/>

            <CardLabelAndNumber label="Overall Time (s)"
                                number={(report.overallHttpCallTime() / 1000.0).toFixed(2)}/>
        </div>
    )
}

export default OverallSummary
