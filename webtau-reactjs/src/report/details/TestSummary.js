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

import TestNameCard from './TestNameCard'
import Card from '../widgets/Card'
import CardWithTime from '../widgets/CardWithTime'

import './TestSummary.css'

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

const TestSummary = ({test}) => {
    const numberOfHttpCalls = test.httpCalls ? test.httpCalls.length : 0

    return (
        <div className="test-summary">
            <TestNameCard test={test}/>

            <div className="test-summary-timing">
                <CardWithTime label="Start Time (Local)"
                              time={test.startTime}/>

                <CardWithTime label="Start Time (UTC)"
                              utc={true}
                              time={test.startTime}/>

                <CardLabelAndNumber label="Execution time (ms)"
                                    number={test.elapsedTime}/>
            </div>

            <div className="test-summary-http-dashboard">
                <NumberOfHttpCalls number={numberOfHttpCalls}/>
                <AverageHttpCallsTime test={test}/>
                <OverallHttpCallsTime test={test}/>
            </div>

            <OptionalPreBlock className="context-description" message={test.contextDescription}/>
            <CardPreMessage message={test.exceptionMessage}/>

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

function CardPreMessage({message}) {
    if (!message) {
        return null
    }

    return (
        <Card className="card-pre-message">
            <pre>
                {message.trim()}
            </pre>
        </Card>
    )
}

export default TestSummary