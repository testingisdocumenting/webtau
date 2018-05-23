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

import NavigationEntry from './NavigationEntry'

import './ListOfHttpCalls.css'

function ListOfHttpCalls({httpCalls, onSelect, selectedIdx}) {
    return (
        <div className="list-of-http-calls">
            {httpCalls.map((httpCall, idx) => <HttpCallEntry key={idx}
                                                             idx={idx}
                                                             httpCall={httpCall}
                                                             onSelect={onSelect}
                                                             isSelected={idx === selectedIdx}/>)}
        </div>
    )
}

function HttpCallEntry({httpCall, idx, onSelect, isSelected}) {
    return (
        <NavigationEntry label={httpCall.label}
                         status={httpCall.status}
                         isSelected={isSelected}
                         onSelect={() => onSelect(idx)}/>
    )
}

export default ListOfHttpCalls
