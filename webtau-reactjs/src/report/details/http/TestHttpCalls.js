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

import React, {Component} from 'react'

import ElapsedTime from './ElapsedTime'
import HttpPayload from './HttpPayload'

import moment from 'moment'

import HttpCallHeaders from './HttpCallHeaders'

import './TestHttpCalls.css'
import '../../widgets/Table.css'

class TestHttpCalls extends Component {
    state = {}

    static stateName = 'httpCallIdxs'

    static getDerivedStateFromProps(props) {
        const callIdxs = props.urlState[TestHttpCalls.stateName]

        const ids = callIdxs ? callIdxs.split('-') : []
        const expandedByIdx = {}
        ids.forEach(id => expandedByIdx[id] = true)

        return {expandedByIdx}
    }

    render() {
        const {test} = this.props;

        return (
            <div className="http">
                <table className="http-table table">
                    <thead>
                    <tr>
                        <th width="35px"/>
                        <th width="60px">Method</th>
                        <th width="40px">Code</th>
                        <th width="60px">Start</th>
                        <th width="60px">Took</th>
                        <th>Url</th>
                    </tr>
                    </thead>
                    <tbody>
                    {test.httpCalls.map((httpCall, idx) => this.renderRow(httpCall, idx))}
                    </tbody>
                </table>
            </div>
        )
    }

    renderRow(httpCall, idx) {
        const {reportNavigation} = this.props

        const hasProblem = httpCall.mismatches.length > 0 || !!httpCall.errorMessage
        const className = 'test-http-call' + (hasProblem ? ' with-problem' : '')
        const isExpanded = this.isExpanded(idx)

        const startDateTime = new Date(httpCall.startTime)

        return (
            <React.Fragment key={idx}>
                <tr className={className} onClick={() => this.onCollapseToggleClick(idx)}>
                    <td className="collapse-toggle">{isExpanded ? '-' : '+'}</td>
                    <td className="method">{httpCall.method}</td>
                    <td className="status-code">{httpCall.responseStatusCode}</td>
                    <td>{moment(startDateTime).local().format('HH:mm:ss.SSS')}</td>
                    <ElapsedTime millis={httpCall.elapsedTime}/>
                    <td className="url">{httpCall.url}</td>
                </tr>

                {isExpanded && <HttpCallDetails httpCall={httpCall} reportNavigation={reportNavigation}/>}
            </React.Fragment>
        )
    }

    isExpanded(idx) {
        return this.state.expandedByIdx.hasOwnProperty(idx)
    }

    onCollapseToggleClick = (idx) => {
        const {onInternalStateUpdate} = this.props;

        const newExpandedByIdx = {...this.state.expandedByIdx}

        if (this.isExpanded(idx)) {
            delete newExpandedByIdx[idx]
        } else {
            newExpandedByIdx[idx] = true
        }

        onInternalStateUpdate({
            [TestHttpCalls.stateName]: Object.keys(newExpandedByIdx).join('-') || ''
        })
    }
}

function HttpCallDetails({httpCall, reportNavigation}) {
    return (
        <tr className="test-http-call-details">
            <td/>
            <td/>
            <td colSpan="4">
                <Mismatches httpCall={httpCall}/>
                <ErrorMessage httpCall={httpCall}/>

                <div className="http-call-header-body">
                    <HttpCallHeaders request={httpCall.requestHeader}
                                     response={httpCall.responseHeader}/>

                    <div className="body-request-response">
                        <Request httpCall={httpCall} reportNavigation={reportNavigation}/>
                        <Response httpCall={httpCall} reportNavigation={reportNavigation}/>
                    </div>
                </div>
            </td>
        </tr>
    )
}

function Mismatches({httpCall}) {
    return httpCall.mismatches.map((m, idx) => <div key={idx} className="mismatch">
        <pre>{m}</pre>
    </div>)
}

function ErrorMessage({httpCall}) {
    if (! httpCall.errorMessage) {
        return null
    }

    return (
        <div className="error-message">
            <pre>{httpCall.errorMessage}</pre>
        </div>
    )
}

function Request({httpCall, reportNavigation}) {
    if (! httpCall.requestBody) {
        return <div/>
    }

    return (
        <div className="request">
            <HttpPayload caption="Request"
                         type={httpCall.requestType}
                         data={httpCall.requestBody}
                         httpCallId={httpCall.id}
                         payloadType='request'
                         onZoom={reportNavigation.zoomInHttpPayload}/>
        </div>
    )
}

function Response({httpCall, reportNavigation}) {
    if (! httpCall.responseBody) {
        return null
    }

    return (
        <div className="response">
            <HttpPayload caption="Response"
                         type={httpCall.responseType}
                         data={httpCall.responseBody}
                         checks={httpCall.responseBodyChecks}
                         httpCallId={httpCall.id}
                         payloadType='response'
                         onZoom={reportNavigation.zoomInHttpPayload}/>
        </div>
    )
}

export default TestHttpCalls