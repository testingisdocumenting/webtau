import React from 'react'

import DetailsTabSelection from './details/DetailsTabSelection'
import NoDetailsDefined from './details/NoDetailsDefined'

import './TestDetails.css'

const TestDetails = ({
                         test,
                         detailTabs,
                         selectedDetailTabName,
                         onDetailsTabSelection,
                         urlState,
                         onInternalStateUpdate
                     }) => {
    const DetailTab = detailTab()
    const tabNames = detailTabs.map(r => r.tabName)

    return (
        <div className="test-details">
            <div className="tabs-details-area">
                <DetailsTabSelection tabs={tabNames}
                                     selectedTabName={selectedDetailTabName}
                                     onTabSelection={onDetailsTabSelection}/>
            </div>

            <div className="detail-area">
                <DetailTab test={test}
                           detailTabName={selectedDetailTabName}
                           urlState={urlState}
                           onInternalStateUpdate={onInternalStateUpdate}/>
            </div>
        </div>
    )

    function detailTab() {
        const entry = detailTabs.filter(r => r.tabName === selectedDetailTabName)
        return entry ? entry[0].component : NoDetailsDefined
    }
}

export default TestDetails