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

import TextPayload from './TextPayload'
import JsonPayload from './JsonPayload'

import './HttpPayload.css'

function PayloadData({type, data, checks}) {
    return type.indexOf('json') !== -1 ?
        <JsonPayload json={JSON.parse(data)} checks={checks}/> :
        <TextPayload text={data}/>
}

function HttpPayload({caption, type, data, checks, httpCallId, payloadType, onZoom}) {
    if (!data) {
        return null
    }

    const fullScreenToggle = onZoom && (
        <div className="fullscreen-icon" onClick={() => onZoom({httpCallId, payloadType})}>
            <FullScreenIcon/>
        </div>
    )

    return (
        <div className="http-payload">
            <div className="caption-and-fullscreen">
                <div className="caption">{caption}</div>
                {fullScreenToggle}
            </div>
            <PayloadData type={type} data={data} checks={checks}/>
        </div>
    )
}

function FullScreenIcon() {
    return (
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
             stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"
             className="feather feather-maximize">
            <path d="M8 3H5a2 2 0 0 0-2 2v3m18 0V5a2 2 0 0 0-2-2h-3m0 18h3a2 2 0 0 0 2-2v-3M3 16v3a2 2 0 0 0 2 2h3"/>
        </svg>
    )
}

export default HttpPayload
