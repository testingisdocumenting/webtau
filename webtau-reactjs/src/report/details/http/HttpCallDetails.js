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
import TestName from '../TestName'

import Card from '../../widgets/Card'
import CardLabelAndNumber from '../../widgets/CardLabelAndNumber'
import CardWithTime from '../../widgets/CardWithTime'

import HttpCallHeaders from './HttpCallHeaders'

import './HttpCallDetails.css'

function HttpCallDetails({httpCall, reportNavigation}) {
    if (! httpCall.test) {
        return <HttpCallSkippedDetails httpCall={httpCall}/>
    }

    return (
        <div className="http-call-details">
            <div className="http-call-time-and-name">
                <CardWithTime label="Start Time (Local)"
                              time={httpCall.startTime}/>

                <CardWithTime label="Start Time (UTC)"
                              utc={true}
                              time={httpCall.startTime}/>

                <CardLabelAndNumber label="Latency (ms)"
                                    number={httpCall.elapsedTime}/>

                <UrlAndTestNameCard httpCall={httpCall} onTestSelect={reportNavigation.selectTest}/>
            </div>

            <Mismatches httpCall={httpCall}/>
            <ErrorMessage httpCall={httpCall}/>

            <HttpCallHeaders useCards="true"
                             request={httpCall.requestHeader}
                             response={httpCall.responseHeader}/>

            <div className="body-request-response">
                <Request httpCall={httpCall} onHttpPayloadZoomIn={reportNavigation.zoomInHttpPayload}/>
                <Response httpCall={httpCall} onHttpPayloadZoomIn={reportNavigation.zoomInHttpPayload}/>
            </div>
        </div>
    )
}

function HttpCallSkippedDetails({httpCall}) {
    return (
        <div className="http-call-details">
            <Card className="http-call-no-details">
                {httpCall.label} was not exercised
            </Card>
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

function Request({httpCall, onHttpPayloadZoomIn}) {
    if (! httpCall.requestBody) {
        return <div/>
    }

    return (
        <Card className="http-call-details-request-details">
            <HttpPayload caption="Request"
                         type={httpCall.requestType}
                         data={httpCall.requestBody}
                         httpCallId={httpCall.id}
                         payloadType='request'
                         onZoom={onHttpPayloadZoomIn}/>
        </Card>
    )
}

function Response({httpCall, onHttpPayloadZoomIn}) {
    if (! httpCall.responseBody) {
        return <div/>
    }

    return (
        <Card className="http-call-details-response-details">
            <HttpPayload caption="Response"
                         type={httpCall.responseType}
                         data={httpCall.responseBody}
                         checks={httpCall.responseBodyChecks}
                         httpCallId={httpCall.id}
                         payloadType='response'
                         onZoom={onHttpPayloadZoomIn}/>
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