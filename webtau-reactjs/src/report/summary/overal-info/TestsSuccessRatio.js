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

import React from 'react'

import CardList from '../../widgets/CardList'
import CardLabelAndNumber from '../../widgets/CardLabelAndNumber'

export default function TestsSuccessRatio({report}) {
    return (
        <CardList label="Tests success ratio">
            <CardLabelAndNumber label="Passed" number={report.summary.percentagePassed} unit="%"/>
            <CardLabelAndNumber label="Passed total" number={report.summary.total}/>
            <CardLabelAndNumber label="Total with Problems" number={report.summary.totalWithProblems}/>
        </CardList>
    )
}