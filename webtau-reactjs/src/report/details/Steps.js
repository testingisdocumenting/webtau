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

import './Steps.css'

const Token = ({type, value}) => {
    const className = "token " + type
    const valueToUse = value + " "

    switch (type) {
        case "url":
            return <a className="token url" href={value}>{value}</a>
        default:
            return <span className={className}>{valueToUse}</span>

    }
}

const Message = ({message}) => {
    return <div className="message">{message.map((t, idx) => <Token key={idx} {...t}/>)}</div>
}

const Step = ({step}) => {
    const children = step.children ? step.children.map((c, idx) => <Step key={idx} step={c}/>) : null;

    return (
        <div className="step">
            <Message message={step.message}/>
            <div className="children">{children}</div>
        </div>
    )
}

const Steps = ({steps}) => {
    return (
        <div className="steps">
            {steps.map((step, idx) => <Step key={idx} step={step}/>)}
        </div>
    )
}

export default Steps
