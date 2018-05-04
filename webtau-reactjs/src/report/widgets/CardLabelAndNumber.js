import React from 'react'

import Card from './Card'
import './CardLabelAndNumber.css'

const CardLabelAndNumber = ({label, secondaryLabel, number}) => {
    return (
        <Card className="card-label-and-number">
            <div className="card-number">{number}</div>
            <div className="card-label">{label}</div>
            <div className="card-secondary-label">{secondaryLabel}</div>
        </Card>
    )
}

export default CardLabelAndNumber
