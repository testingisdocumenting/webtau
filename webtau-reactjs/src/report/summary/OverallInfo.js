/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

import HttpCallsTiming from './overal-info/HttpCallsTiming'
import HttpOperationCoverageSummary from './overal-info/HttpOperationCoverageSummary'
import TestsOverallTiming from './overal-info/TestsOverallTiming'

import TestsSuccessRatio from './overal-info/TestsSuccessRatio'
import TestsRanRatio from './overal-info/TestsRanRatio'

import './OverallInfo.css'

export default function OverallInfo({report, onSwitchToHttpCalls, onSwitchToSkippedHttpCalls}) {
    return (
        <div className="overall-info">
            <TestsOverallTiming report={report}/>
            <TestsSuccessRatio report={report}/>
            <HttpOperationCoverageSummary report={report} onSwitchToSkippedHttpCalls={onSwitchToSkippedHttpCalls}/>
            <TestsRanRatio report={report}/>
            <HttpCallsTiming report={report} onSwitchToHttpCalls={onSwitchToHttpCalls}/>
        </div>
    )
}