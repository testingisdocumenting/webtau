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
import HttpHeader from './HttpHeader'

export function httpHeaderDemo(registry) {
    registry.add('with long key', () => (
        <div style={{width: 400}}>
            <HttpHeader header={httpHeaderLongKey()}/>
        </div>
    ))
    registry.add('with long value', () => (
        <div style={{width: 400}}>
            <HttpHeader header={httpHeaderLongValue()}/>
        </div>
    ))
}

function httpHeaderLongKey() {
    return [
        {key: 'AuthorizationAuthorizationAuthorizationAuthorizationAuthorizationAuthorization', value: '********'},
        {
            key: 'X-something',
            value: 'value'
        },
    ]
}

function httpHeaderLongValue() {
    return [
        {key: 'Authorization', value: '********'},
        {
            key: 'X-something',
            value: 'long_nonbreakable_value_long_long_nonbreakable_value_long_long_nonbreakable_value_long'
        },
    ]
}
