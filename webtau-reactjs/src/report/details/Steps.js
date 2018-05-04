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
