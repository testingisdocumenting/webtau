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
import {Step} from './Step'

export function stepsDemo(registry) {
    registry.add('no children', () => <Step step={noChildren()} isTopLevel={true}/>)
    registry.add('with children',() => <Step step={withChildren()} isTopLevel={true}/>)
}

function noChildren() {
    return {
        "elapsedTime": 200,
        "message": [{
            "type": "action",
            "value": "executed HTTP GET"
        }, {
            "type": "url",
            "value": "http://localhost:8080/customers/1"
        }]
    }
}

function withChildren() {
    return {
        ...noChildren(),
        "children": [{
            "elapsedTime": 50,
            "message": [{
                "type": "id",
                "value": "body"
            }, {
                "type": "matcher",
                "value": "equals {firstName=FN, lastName=LN}\nmatches:\n\nbody.firstName:   actual: \"FN\" <java.lang.String>\n                expected: \"FN\" <java.lang.String>\nbody.lastName:   actual: \"LN\" <java.lang.String>\n               expected: \"LN\" <java.lang.String>"
            }]
        }, {
            "elapsedTime": 150,
            "message": [{
                "type": "id",
                "value": "header.statusCode"
            }, {
                "type": "matcher",
                "value": "equals 200\nmatches:\n\nheader.statusCode:   actual: 200 <java.lang.Integer>\n                   expected: 200 <java.lang.Integer>"
            }]
        }]
    }
}