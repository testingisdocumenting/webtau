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

import StatusEnum from './StatusEnum'

export default class PerformanceReport {
    constructor(report) {
        this.httpCalls = report.httpCalls
        this.httpCallsById = report.httpCallsById

        this.openApiHttpCallIdsPerOperation = report.openApiHttpCallIdsPerOperation
        this.notFailedHttpCalls = withoutFailedCalls(report.httpCalls)
        this.sortedNotFailedHttpCalls = sortByLatency(this.notFailedHttpCalls)

        this._calcOverallPercentile()
        this._calcPerformancePerOperation()
    }

    _calcOverallPercentile() {
        const hasNonFailedTests = this.sortedNotFailedHttpCalls.length > 0

        this.maxLatency = hasNonFailedTests ?
            this.sortedNotFailedHttpCalls[this.sortedNotFailedHttpCalls.length - 1].elapsedTime :
            0

        this.percentile = {}

        const steps = [10, 25, 50, 75, 90, 95, 99]
        steps.forEach(percentile => {
            this.percentile[percentile] = hasNonFailedTests ?
                calcPercentile(this.sortedNotFailedHttpCalls, percentile) : 0
        })
    }

    _calcPerformancePerOperation() {
        this.performancePerOperation = sortByCount(
            this.openApiHttpCallIdsPerOperation.map(callIdsAndOperation => {
                return this._calcOperationPerformance(callIdsAndOperation)
            }))
    }

    _calcOperationPerformance({method, url, httpCallIds}) {
        const httpCalls = httpCallIds.map(id => this.httpCallsById[id])
        const sortedHttpCalls = sortByLatency(httpCalls)

        return {
            method,
            url,
            count: httpCalls.length,
            fastest: sortedHttpCalls[0].elapsedTime,
            slowest: sortedHttpCalls[sortedHttpCalls.length - 1].elapsedTime,
            percentile: {
                50: calcPercentile(sortedHttpCalls, 50),
                75: calcPercentile(sortedHttpCalls, 75)
            }
        }
    }
}

function sortByLatency(httpCalls) {
    const sorted = [...httpCalls]
    sorted.sort((a, b) => a.elapsedTime - b.elapsedTime)

    return sorted
}

function sortByCount(entries) {
    const sorted = [...entries]
    sorted.sort((a, b) => b.count - a.count)

    return sorted
}

function calcPercentile(sortedHttpCalls, percentile) {
    const idx = Math.ceil((percentile / 100.0) * sortedHttpCalls.length) - 1
    return {idx, value: sortedHttpCalls[idx].elapsedTime}
}

function withoutFailedCalls(httpCalls) {
    return httpCalls.filter(c => c.status === StatusEnum.PASSED)
}
