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

import { TableCellType, SortableTable, TableValuesRenderer, TableRow } from '../widgets/SortableTable';

import '../widgets/Table.css';
import ElapsedTime from '../widgets/ElapsedTime';
import { WebTauTest } from '../WebTauTest';

interface Props {
  tests: WebTauTest[];
}

const innerTableRenderer: TableValuesRenderer = {
  cellRenderer,
};

const outerTableRenderer: TableValuesRenderer = {
  cellRenderer,
  expandedRowRenderer(row) {
    return <SortableTable header={nestedHeader()} data={row[3] as TableRow[]} renderer={innerTableRenderer} />;
  },
  canBeExpanded() {
    return true;
  },
};

export function TestsPerformance({ tests }: Props) {
  return (
    <div className="webtau-overall-performance">
      <SortableTable
        header={outerHeader()}
        data={prepareData(tests)}
        renderer={outerTableRenderer}
        rowKeyProvider={keyProvider}
      />
    </div>
  );
}

function keyProvider(row: TableRow): string {
  return row[0] as string;
}

function cellRenderer(columnName: string, value: TableCellType): JSX.Element {
  if (columnName === 'Total Elapsed Time' || columnName === 'Elapsed Time') {
    return <ElapsedTime millis={value as number} />;
  }

  return <>{value}</>;
}

function outerHeader() {
  return ['Suite Name', 'Number Of Tests', 'Total Elapsed Time'];
}

function nestedHeader() {
  return ['Scenario Name', 'Elapsed Time'];
}

type GroupedTests = { tests: WebTauTest[]; id: string }[];

function prepareData(tests: WebTauTest[]) {
  // @ts-ignore
  const groupedTests: GroupedTests = Report.groupTestsByContainer(tests);

  return groupedTests.map((group) => {
    const groupElapsed = group.tests.reduce((total, test) => total + test.elapsedTime, 0);

    const innerTests = group.tests.map((t) => [t.scenario, t.elapsedTime]);
    return [group.id, group.tests.length, groupElapsed, innerTests];
  });
}
