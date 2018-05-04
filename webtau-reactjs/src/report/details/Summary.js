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
import SourceCode from './SourceCode'

import CardLabelAndNumber from '../widgets/CardLabelAndNumber'
import NumberOfHttpCalls from '../dashboard/NumberOfHttpCalls'

import Report from '../Report'

import './Summary.css'

const OptionalPreBlock = ({className, message}) => {
    if (!message) {
        return null
    }

    return (
        <div className={className}>
            <pre>
                {message}
            </pre>
        </div>
    )
}

const Summary = ({test}) => {
    const numberOfHttpCalls = test.httpCalls ? test.httpCalls.length : 0

    return (
        <div className="single-summary">
            <div className="file-name-and-scenario">
                <div className="file-name">
                    {test.fileName}
                </div>

                <div className="scenario">
                    {test.scenario}
                </div>
            </div>

            <div className="single-summary-dashboard">
                <NumberOfHttpCalls number={numberOfHttpCalls}/>
                <AverageHttpCallsTime test={test}/>
                <OverallHttpCallsTime test={test}/>
            </div>

            <OptionalPreBlock className="context-description" message={test.contextDescription}/>
            <OptionalPreBlock className="assertion" message={test.assertion}/>
            {
                !test.assertion ? <OptionalPreBlock className="exception-message" message={test.exceptionMessage}/> :
                    null
            }

            {test.failedCodeSnippets && test.failedCodeSnippets.map((cs, idx) => <SourceCode key={idx} {...cs}/>)}
        </div>
    )
}

function OverallHttpCallsTime({test}) {
    if (!test.httpCalls) {
        return null
    }

    return (
        <CardLabelAndNumber label="Overall Time (ms)"
                            number={Report.overallHttpCallTimeForTest(test)}/>
    )
}

function AverageHttpCallsTime({test}) {
    if (!test.httpCalls) {
        return null
    }

    return (
        <CardLabelAndNumber label="Average Time (ms)"
                            number={Report.averageHttpCallTimeForTest(test).toFixed(2)}/>
    )
}

export default Summary