import React from 'react'

import './DetailsTabSelection.css'

const DetailsTabSelection = ({tabs, selectedTabName, onTabSelection}) => {
    return (
        <div className="details-tab-selection">
            <div className="tab-names">
                {tabs.map(t => {
                    const className = "tab-name" + (selectedTabName === t ? " selected" : "")
                    return <div key={t} className={className} onClick={() => onTabSelection(t)}>{t}</div>
                })}
            </div>
        </div>
    )
}

export default DetailsTabSelection
