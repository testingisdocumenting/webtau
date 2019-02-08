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

import HttpPayloadByType from './HttpPayloadByType'
import FullScreenPayload from './FullScreenPayload'

import TabSelection from '../widgets/TabSelection'
import RawHttpPayload from './RawHttpPayload'

import './FullScreenHttpPayload.css'

const tabNames = ['Tree', 'Raw']

export default class FullScreenHttpPayload extends React.Component {
    state = {selectedTabName: tabNames[0]}

    render() {
        const {onClose} = this.props
        const {selectedTabName} = this.state

        return (
            <FullScreenPayload onClose={onClose}>
                <div className="full-screen-http-payload">
                    <div className="full-screen-http-payload-tabs">
                        <TabSelection tabs={tabNames} selectedTabName={selectedTabName} onTabSelection={this.onTabSelection}/>
                    </div>

                    <div className="full-screen-http-payload-content">
                        {this.renderTabContent()}
                    </div>
                </div>

            </FullScreenPayload>
        )
    }

    renderTabContent() {
        const {selectedTabName} = this.state

        switch (selectedTabName) {
            case 'Tree':
                return this.renderTreePayload()
            case 'Raw':
                return this.renderRawPayload()
            default:
                return ''
        }
    }

    renderTreePayload() {
        const {payloadType} = this.props
        const httpCall = this.httpCall

        return (
            <React.Fragment>
                <div className="full-screen-http-payload-url">
                    <a href={httpCall.url}
                       target="_blank"
                       rel="noopener noreferrer">{httpCall.url}</a>
                </div>


                <HttpPayloadByType httpCall={httpCall} payloadType={payloadType}/>
            </React.Fragment>
        )
    }

    renderRawPayload() {
        return (
            <div className="full-screen-http-payload-raw">
                <RawHttpPayload payload={this.payloadDataByType()}/>
            </div>
        )
    }

    payloadDataByType() {
        const {payloadType} = this.props
        const httpCall = this.httpCall

        switch (payloadType) {
            case 'request':
                return isJson(httpCall.requestType) ?
                    prettyPrintJson(httpCall.requestBody):
                    httpCall.requestBody
            case 'response':
                return isJson(httpCall.responseType) ?
                    prettyPrintJson(httpCall.responseBody):
                    httpCall.responseBody
            default:
                return {}

        }

        function isJson(type) {
            return type.indexOf('json') !== -1
        }

        function prettyPrintJson(jsonText) {
            return JSON.stringify(JSON.parse(jsonText), null, 2)
        }
    }

    get httpCall() {
        const {report, payloadHttpCallId} = this.props
        return report.findHttpCallById(payloadHttpCallId)
    }

    onTabSelection = (tabName) => {
        this.setState({selectedTabName: tabName})
    }
}
