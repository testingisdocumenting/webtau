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

import CliOutputCard from "./CliOutputCard"

import TestErrorMessage from "../../widgets/TestErrorMessage"

import './CliCallDetails.css';

export default function CliCallDetails({cliCall}) {
    return (
        <tr className="cli-command-details">
            <td/>
            <td colSpan={4}>
                <Mismatches cliCall={cliCall}/>
                <ErrorMessage cliCall={cliCall}/>

                <div className="cli-command-details-std-output-label">standard output</div>
                <CliOutputCard output={cliCall.out} matchedLines={cliCall.outMatches}/>
                <div className="cli-command-details-err-output-label">error output</div>
                <CliOutputCard output={cliCall.err} matchedLines={cliCall.errMatches}/>
            </td>
        </tr>
    )
}

function Mismatches({cliCall}) {
    return cliCall.mismatches.map((m, idx) => <div key={idx} className="mismatch">
        <TestErrorMessage message={m}/>
    </div>)
}

function ErrorMessage({cliCall}) {
    if (!cliCall.errorMessage) {
        return null
    }

    return (
        <div className="cli-call-details-error-message">
            <TestErrorMessage message={cliCall.errorMessage}/>
        </div>
    )
}
