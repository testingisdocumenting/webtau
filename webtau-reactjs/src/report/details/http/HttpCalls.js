import React, {Component} from 'react'

import ElapsedTime from './ElapsedTime'
import Payload from './Payload'

import './HttpCalls.css'

class HttpCalls extends Component {
    state = {}

    static stateName = 'httpCall5Idxs'
    static getDerivedStateFromProps(props) {
        const callIdxs = props.urlState[HttpCalls.stateName]

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
        const className = 'http-call' + (httpCall.mismatches.length > 0 ? ' with-mismatches' : '')
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
            [HttpCalls.stateName]: Object.keys(newExpandedByIdx).join('-') || ''
        })
    }
}

function HttpCallDetails({httpCall}) {
    const mismatches = httpCall.mismatches.map((m, idx) => <div key={idx} className="mismatch"><pre>{m}</pre></div>)

    return (
        <tr className="http-call-details">
            <td/>
            <td/>
            <td/>
            <td/>
            <td>
                {mismatches}
                <div className="request">
                    <Payload caption="Request"
                             type={httpCall.requestType}
                             data={httpCall.requestBody}/>
                </div>

                <div className="response">
                    <Payload caption="Response"
                             type={httpCall.responseType}
                             data={httpCall.responseBody}
                             checks={httpCall.responseBodyChecks}/>
                </div>
            </td>
        </tr>
    )
}

export default HttpCalls