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

import HttpPayload from './HttpPayload'
import Card from '../../widgets/Card'
import TestName from '../TestName'

import './HttpCallDetails.css'
import CardLabelAndNumber from '../../widgets/CardLabelAndNumber'

function HttpCallDetails({httpCall, onTestSelect}) {
    return (
        <div className="http-call-details">
            <div className="http-call-latency-and-name">
                <CardLabelAndNumber label="Latency (ms)"
                                    number={httpCall.elapsedTime}/>
                <UrlAndTestNameCard httpCall={httpCall} onTestSelect={onTestSelect}/>
            </div>

            <Mismatches httpCall={httpCall}/>
            <ErrorMessage httpCall={httpCall}/>

            <div className="request-response">
                <Request httpCall={httpCall}/>
                <Response httpCall={httpCall}/>
            </div>
        </div>
    )
}

function Mismatches({httpCall}) {
    if (httpCall.mismatches.length === 0) {
        return null
    }

    const mismatches = httpCall.mismatches.map((m, idx) => <div key={idx} className="mismatch">
        <pre>{m}</pre>
    </div>)

    return (
        <Card className="http-call-details-mismatches">
            {mismatches}
        </Card>
    )
}

function ErrorMessage({httpCall}) {
    if (!httpCall.errorMessage) {
        return null
    }

    return (
        <Card className="http-call-details-error-message">
            {httpCall.errorMessage}
        </Card>
    )
}

function Request({httpCall}) {
    if (! httpCall.requestBody) {
        return null
    }

    return (
        <Card className="http-call-details-request-details">
            <HttpPayload caption="Request"
                         type={httpCall.requestType}
                         data={httpCall.requestBody}/>
        </Card>
    )
}

function Response({httpCall}) {
    if (! httpCall.responseBody) {
        return null
    }

    return (
        <Card className="http-call-details-response-details">
            <HttpPayload caption="Response"
                         type={httpCall.responseType}
                         data={httpCall.responseBody}
                         checks={httpCall.responseBodyChecks}/>
        </Card>
    )
}

function UrlAndTestNameCard({httpCall, onTestSelect}) {
    return (
        <Card>
            <Url httpCall={httpCall}/>
            <TestName test={httpCall.test} onTestClick={onTestSelect}/>
        </Card>
    )
}

function Url({httpCall}) {
    return (
        <div className="http-call-details-url">
            <div className="method">{httpCall.method}</div>
            <div className="url">
                <a href={httpCall.url} target="_blank">{httpCall.url}</a>
            </div>
        </div>
    )
}

export default HttpCallDetails