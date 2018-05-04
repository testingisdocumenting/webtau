import React from 'react'

import TextPayload from './TextPayload'
import JsonPayload from './JsonPayload'

const PayloadData = ({type, data, checks}) => {
    return type.indexOf('json') !== -1 ?
        <JsonPayload json={JSON.parse(data)} checks={checks}/> :
        <TextPayload text={data}/>
}

const Payload = ({caption, type, data, checks}) => {
    if (! data) {
        return null
    }

    return (
        <div className="payload">
            <div className="caption">{caption}</div>
            <PayloadData type={type} data={data} checks={checks}/>
        </div>
    )
}

export default Payload
