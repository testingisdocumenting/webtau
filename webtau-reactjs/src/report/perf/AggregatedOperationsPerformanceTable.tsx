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
import { SortableTable } from '../widgets/SortableTable';
import { AggregatedOperationPerformance } from './AggregatedOperationPerformance';

interface Props {
  operations: AggregatedOperationPerformance[];
}

export function AggregatedOperationsPerformanceTable({ operations }: Props) {
  return <SortableTable className="operations-performance" header={header()} data={prepareData(operations)} />;
}

function header() {
  return ['Operation', 'Count', 'Min', 'Max', 'Mean', '20 %', '50 %', '80 %', '95 %', '99 %'];
}

function prepareData(operations: AggregatedOperationPerformance[]) {
  return operations.map((e) => [
    e.groupId,
    e.count,
    e.minMs.toFixed(2),
    e.maxMs.toFixed(2),
    e.averageMs.toFixed(2),
    e.p20ms.toFixed(2),
    e.p50ms.toFixed(2),
    e.p80ms.toFixed(2),
    e.p95ms.toFixed(2),
    e.p99ms.toFixed(2),
  ]);
}
