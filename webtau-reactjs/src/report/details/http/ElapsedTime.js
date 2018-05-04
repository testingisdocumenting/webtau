import React from 'react'

import './ElapsedTime.css'

function ElapsedTime({millis}) {
    const seconds = (millis / 1000) | 0
    const remainingMs = millis % 1000

    return (
        <td className="http-call-elapsed-time">
            <Seconds seconds={seconds}/>
            <Millis millis={remainingMs}/>
        </td>
    )
}

function Seconds({seconds}) {
    if (seconds === 0) {
        return null
    }

    return (
        <React.Fragment>
            <span className="elapsed-seconds">{seconds}</span>
            <span className="time-unit">s</span>
        </React.Fragment>
    )
}

function Millis({millis}) {
    if (millis === 0) {
        return null
    }

    return (
        <React.Fragment>
            <span className="elapsed-millis">{millis}</span>
            <span className="time-unit">ms</span>
        </React.Fragment>
    )
}

export default ElapsedTime
