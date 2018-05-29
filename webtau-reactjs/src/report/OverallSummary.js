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

import NumberOfHttpCalls from './dashboard/NumberOfHttpCalls'
import CardLabelAndNumber from './widgets/CardLabelAndNumber'

import './OverallSummary.css'

function OverallSummary({report, onSwitchToHttpCalls, onSwitchToSkippedHttpCalls}) {
    return (
        <div className="overall-summary">
            <CallsTiming report={report} onSwitchToHttpCalls={onSwitchToHttpCalls}/>
            <UrlCoverageSummary report={report} onSwitchToSkippedHttpCalls={onSwitchToSkippedHttpCalls}/>
        </div>
    )
}

function CallsTiming({report, onSwitchToHttpCalls}) {
    return (
        <div className="overall-http-calls-time">
            <div className="overall-number-of-http-calls" onClick={onSwitchToHttpCalls}>
                <NumberOfHttpCalls number={report.numberOfHttpCalls()}/>
            </div>

            <CardLabelAndNumber label="Average Time (ms)"
                                number={report.averageHttpCallTime().toFixed(2)}/>

            <CardLabelAndNumber label="Overall Time (s)"
                                number={(report.overallHttpCallTime() / 1000.0).toFixed(2)}/>
        </div>
    )
}

function UrlCoverageSummary({report, onSwitchToSkippedHttpCalls}) {
    if (! report.hasUrlCoverage()) {
        return null
    }

    const urlCoveragePercentage = (report.openApiUrlsCoverage() * 100).toFixed(2) + ' %'
    return (
        <div className="overall-http-calls-coverage">
            <CardLabelAndNumber label="Open API urls coverage"
                                number={urlCoveragePercentage}/>
            <CardLabelAndNumber label="Covered urls"
                                number={report.numberOfOpenApiCoveredUrls()}/>

            <div className="overall-number-of-skipped" onClick={onSwitchToSkippedHttpCalls}>
                <CardLabelAndNumber label="Skipped urls"
                                    number={report.numberOfOpenApiSkippedUrls()}
                                    onClick={onSwitchToSkippedHttpCalls}/>
            </div>
        </div>
    )
}

export default OverallSummary
