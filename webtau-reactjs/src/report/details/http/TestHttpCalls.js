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

import './TestHttpCalls.css'

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
                <table className="http-table">
                    <thead>
                    <tr>
                        <th width="35px"/>
                        <th width="60px">Method</th>
                        <th width="40px">Code</th>
                        <th width="60px">Time</th>
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
        const className = 'test-http-call' + (httpCall.mismatches.length > 0 ? ' with-mismatches' : '')
        const isExpanded = this.isExpanded(idx)

        return (
            <React.Fragment key={idx}>
                <tr className={className} onClick={() => this.onCollapseToggleClick(idx)}>
                    <td className="collapse-toggle">{isExpanded ? '-' : '+'}</td>
                    <td className="method">{httpCall.method}</td>
                    <td className="status-code">{httpCall.responseStatusCode}</td>
                    <ElapsedTime millis={httpCall.elapsedTime}/>
                    <td className="url">{httpCall.url}</td>
                </tr>

                {isExpanded && <HttpCallDetails httpCall={httpCall}/>}
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

function HttpCallDetails({httpCall}) {
    const mismatches = httpCall.mismatches.map((m, idx) => <div key={idx} className="mismatch">
        <pre>{m}</pre>
    </div>)

    return (
        <tr className="test-http-call-details">
            <td/>
            <td/>
            <td/>
            <td/>
            <td>
                {mismatches}
                <div className="request-response">
                    <Request httpCall={httpCall}/>
                    <Response httpCall={httpCall}/>
                </div>
            </td>
        </tr>
    )
}

function Request({httpCall}) {
    if (! httpCall.requestBody) {
        return null
    }

    return (
        <div className="request">
            <HttpPayload caption="Request"
                         type={httpCall.requestType}
                         data={httpCall.requestBody}/>
        </div>
    )
}

function Response({httpCall}) {
    if (! httpCall.responseBody) {
        return null
    }

    return (
        <div className="response">
            <HttpPayload caption="Response"
                         type={httpCall.responseType}
                         data={httpCall.responseBody}
                         checks={httpCall.responseBodyChecks}/>
        </div>
    )
}

export default TestHttpCalls