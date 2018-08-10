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

import StatusEnum from './StatusEnum'

export default class PerformanceReport {
    constructor(httpCalls) {
        this.httpCalls = httpCalls
        this.notFailedHttpCalls = withoutFailedCalls(httpCalls)
        this.sortedNotFailedHttpCalls = sortedByLatency(this.notFailedHttpCalls)

        this.maxLatency = this.sortedNotFailedHttpCalls[this.sortedNotFailedHttpCalls.length - 1].elapsedTime

        this.percentile = {}

        const steps = [10, 25, 50, 75, 90, 95, 99]
        steps.forEach(percentile => {
            this.percentile[percentile] = this._calcPercentile(percentile)
        })
    }

    _calcPercentile(percentile) {
        const idx = Math.ceil((percentile / 100.0) * this.notFailedHttpCalls.length) - 1
        return {idx, value: this.sortedNotFailedHttpCalls[idx].elapsedTime}
    }
}

function sortedByLatency(httpCalls) {
    const sorted = [...httpCalls]
    sorted.sort((a, b) => a.elapsedTime - b.elapsedTime)

    return sorted
}

function withoutFailedCalls(httpCalls) {
    return httpCalls.filter(c => c.status === StatusEnum.PASSED)
}
