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

import CollapsibleHttpHeader from '../http-header/CollapsibleHttpHeader'
import CardWithPadding from '../../widgets/CardWithPadding'

import './HttpCallHeaders.css'

export default class HttpCallHeaders extends React.Component {
    state = {
        isCollapsed: true
    }

    render() {
        const {request, response, useCards} = this.props
        const {isCollapsed} = this.state

        const renderedRequest = <CollapsibleHttpHeader label="Request Header"
                                                       header={request}
                                                       isCollapsed={isCollapsed}
                                                       onToggle={this.toggle}/>

        const renderedResponse = <CollapsibleHttpHeader label="Response Header"
                                                        header={response}
                                                        isCollapsed={isCollapsed}
                                                        onToggle={this.toggle}/>

        const wrappedRequest = useCards ?
            <CardWithPadding>{renderedRequest}</CardWithPadding> :
            renderedRequest

        const wrappedResponse = useCards ?
            <CardWithPadding>{renderedResponse}</CardWithPadding> :
            renderedResponse

        return (
            <div className="http-call-headers">
                {wrappedRequest}
                {wrappedResponse}
            </div>
        )
    }

    toggle = () => {
        this.setState(prev => ({isCollapsed: !prev.isCollapsed}))
    }
}