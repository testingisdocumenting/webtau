/*
 * Copyright 2021 webtau maintainers
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

import React from 'react';
import { SortableTable, TableRow, TableValuesRenderer } from './SortableTable';
import { Registry } from 'react-component-viewer';

export function sortableTableDemo(registry: Registry) {
  registry
    .add('no title', () => <SortableTable header={header()} data={data()} />)
    .add('with expand', () => (
      <SortableTable
        header={header()}
        data={data()}
        renderer={renderer()}
        rowKeyProvider={(row) => row[0].toString()}
      />
    ));
}

function header() {
  return ['Column A', 'Column B', 'Column C'];
}

function data() {
  return [
    ['value a1', 20, 40, 'extra details one'],
    ['value a2', 30, 50, 'extra details two'],
  ];
}

function renderer(): TableValuesRenderer {
  return {
    canBeExpanded() {
      return true;
    },

    expandedRowRenderer(row: TableRow) {
      return <div>{row[3]}</div>;
    },
  };
}
