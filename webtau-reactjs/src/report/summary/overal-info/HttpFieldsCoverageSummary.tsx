/*
 * Copyright 2022 webtau maintainers
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

import React from 'react';

import { CardLabelAndNumber } from '../../widgets/CardLabelAndNumber';
import CardList from '../../widgets/CardList';
import { HttpOperationCoverage } from '../HttpDataCoverageTab';

interface Props {
  httpDataCoverage?: HttpOperationCoverage[];
  onSwitchToHttpDataCoverage(): void;
}

export function HttpDataCoverageSummary({ httpDataCoverage, onSwitchToHttpDataCoverage }: Props) {
  if (!httpDataCoverage) {
    return null;
  }

  const summary = calcSummary(httpDataCoverage);

  return (
    <CardList label="HTTP Data Coverage" onClick={onSwitchToHttpDataCoverage}>
      <CardLabelAndNumber label="Percentage" number={summary.percent.toFixed(2) + ' %'} />
      <CardLabelAndNumber label="Touched Paths" number={summary.touchedCount} />
      <CardLabelAndNumber label="Untouched Paths" number={summary.untouchedCount} />
    </CardList>
  );
}

function calcSummary(httpCoverage: HttpOperationCoverage[]) {
  let touchedCount = 0;
  let untouchedCount = 0;

  for (let idx = 0; idx < httpCoverage.length; idx++) {
    const coverage = httpCoverage[idx];
    touchedCount += coverage.touchedPathsCount;
    untouchedCount += coverage.untouchedPathsCount;
  }

  let total = touchedCount + untouchedCount;
  let percent = (touchedCount / total) * 100;

  return {
    touchedCount,
    untouchedCount,
    percent
  }
}