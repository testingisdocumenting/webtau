/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

import TestHttpCalls from './details/http/TestHttpCalls'
import NavigationEntriesType from './navigation/NavigationEntriesType'
import TestCliCalls from "./details/cli/TestCliCalls"

class WebTauReportStateCreator {
    constructor(report) {
        this.report = report
    }

    static createEmptyFullState() {
        return {
            testId: '',
            detailTabName: '',
            statusFilter: '',
            filterText: '',
            entriesType: '',
            payloadType: '',
            httpCallId: '',
            [TestHttpCalls.stateName]: '',
            [TestCliCalls.stateName]: ''
        }
    }

    stateFromUrl(url) {
        const searchParams = WebTauReportStateCreator._searchParamsFromUrl(url)

        const entriesType = searchParams.entriesType || NavigationEntriesType.TESTS

        const testIdFromParam = searchParams.testId
        const testId = this.report.hasTestWithId(testIdFromParam) ? testIdFromParam : undefined

        const httpCallId = searchParams.httpCallId ? searchParams.httpCallId : undefined

        const detailTabFromParam = searchParams.detailTabName
        const detailTabName = this.report.hasDetailWithTabName(testId, detailTabFromParam) ?
            detailTabFromParam:
            this.report.firstDetailTabName(testId)

        const statusFilter = searchParams.statusFilter

        return {
            ...WebTauReportStateCreator.createEmptyFullState(),
            ...searchParams,
            entriesType,
            testId,
            httpCallId,
            detailTabName,
            statusFilter
        }
    }

    buildUrlSearchParams(state) {
        const searchParams = new URLSearchParams();

        Object.keys(state).forEach(k => {
            const v = state[k]
            if (v !== undefined) {
                searchParams.set(k, v.toString())
            }
        });

        return searchParams.toString();
    }

    static _searchParamsFromUrl(url) {
        const result = {}
        const searchParams = new URLSearchParams(url)
        for (let p of searchParams) {
            result[p[0]] = p[1]
        }

        return result
    }
}

export default WebTauReportStateCreator