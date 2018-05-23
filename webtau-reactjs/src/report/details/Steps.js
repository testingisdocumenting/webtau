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

import Card from '../widgets/Card'

import './Steps.css'

function Steps({steps}) {
    return (
        <div className="steps">
            {steps.map((step, idx) => <Step key={idx} step={step} isTopLevel={true}/>)}
        </div>
    )
}

function Step({step, isTopLevel}) {
    const children = step.children ? step.children.map((c, idx) => <Step key={idx} step={c}/>) : null;

    const ParentContainer = isTopLevel ? Card : 'div'
    return (
        <ParentContainer className="step-card">
            <Message message={step.message}/>
            <div className="steps-children">{children}</div>
        </ParentContainer>
    )
}

function Message({message}) {
    return <div className="message">{message.map((t, idx) => <Token key={idx} {...t}/>)}</div>
}

function Token({type, value}) {
    const className = 'step-token ' + type
    const valueToUse = value + ' '

    switch (type) {
        case 'url':
            return <a className="step-token url" href={value}>{value}</a>
        default:
            return <span className={className}>{valueToUse}</span>

    }
}

export default Steps
