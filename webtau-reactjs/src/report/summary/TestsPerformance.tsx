/*
 * Copyright 2021 webtau maintainers
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
import Report from '../Report';

import { CellType, SortableTable } from '../widgets/SortableTable';

import '../widgets/Table.css';
import ElapsedTime from '../widgets/ElapsedTime';

interface Props {
  report: Report;
}

export function TestsPerformance({ report }: Props) {
  return (
    <div className="webtau-overall-performance">
      <SortableTable header={header()} data={prepareData(report)} renderConverter={renderConverter} />
    </div>
  );

  function renderConverter(columnName: string, value: CellType): JSX.Element {
    if (columnName === 'Total Elapsed Time') {
      return <ElapsedTime millis={value as number} />;
    }

    return <>value</>;
  }
}

function header() {
  return ['Suite Name', 'Number Of Tests', 'Total Elapsed Time'];
}

interface Test {
  elapsedTime: number;
}

type GroupedTests = { tests: Test[]; id: string }[];

function prepareData(report: Report) {
  // @ts-ignore
  const groupedTests: GroupedTests = Report.groupTestsByContainer(report.tests);

  return groupedTests.map((group) => {
    const groupElapsed = group.tests.reduce((total, test) => total + test.elapsedTime, 0);

    return [group.id, group.tests.length, groupElapsed];
  });
}
