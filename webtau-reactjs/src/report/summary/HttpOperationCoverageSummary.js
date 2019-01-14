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

import CardLabelAndNumber from '../widgets/CardLabelAndNumber'
import CardList from '../widgets/CardList'

export default function HttpOperationCoverageSummary({report, onSwitchToSkippedHttpCalls}) {
    if (! report.hasHttpOperationCoverage()) {
        return null
    }

    const operationCoveragePercentage = (report.openApiOperationsCoverage() * 100).toFixed(2) + ' %'
    return (
        <CardList label="HTTP Coverage">
            <CardLabelAndNumber label="Operations coverage"
                                number={operationCoveragePercentage}/>
            <CardLabelAndNumber label="Covered operations"
                                number={report.numberOfOpenApiCoveredOperations()}/>

            <div className="overall-number-of-skipped" onClick={onSwitchToSkippedHttpCalls}>
                <CardLabelAndNumber label="Skipped operations"
                                    number={report.numberOfOpenApiSkippedOperations()}
                                    onClick={onSwitchToSkippedHttpCalls}/>
            </div>
        </CardList>
    )
}
