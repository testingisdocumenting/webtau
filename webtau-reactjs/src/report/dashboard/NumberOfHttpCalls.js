import React from 'react'

import CardLabelAndNumber from '../widgets/CardLabelAndNumber'

const NumberOfHttpCalls = ({number}) => {
    if (!number) {
        return null
    }

    const label = number === 1 ? 'HTTP call' : 'HTTP calls'

    return (
        <CardLabelAndNumber label={label} number={number}/>
    )
}

export default NumberOfHttpCalls

