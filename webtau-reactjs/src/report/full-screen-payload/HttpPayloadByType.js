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

import HttpPayload from '../details/http/HttpPayload'

export default function HttpPayloadByType({httpCall, payloadType}) {
    switch (payloadType) {
        case 'request':
            return (
                <HttpPayload caption="Request"
                             type={httpCall.requestType}
                             data={httpCall.requestBody}/>
            )
        case 'response':
            return (
                <HttpPayload caption="Response"
                             type={httpCall.responseType}
                             data={httpCall.responseBody}
                             checks={httpCall.responseBodyChecks}/>
            )
        default:
            return <div>unsupported payload type: {payloadType}</div>

    }
}