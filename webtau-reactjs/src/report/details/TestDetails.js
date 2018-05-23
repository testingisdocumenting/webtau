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

import DetailsTabSelection from './DetailsTabSelection'
import NoDetailsDefined from './NoDetailsDefined'

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