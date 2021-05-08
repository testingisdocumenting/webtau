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

import { Registry } from 'react-component-viewer';
import { AggregatedOperationsPerformanceTable } from './AggregatedOperationsPerformanceTable';

export function aggregatedOperationsPerformanceTableDemo(registry: Registry) {
  registry.add('perf table', () => <AggregatedOperationsPerformanceTable operations={samplePerfData()} />);
}

function samplePerfData() {
  return [
    {
      groupId: 'GET http://operation',
      minMs: 10,
      maxMs: 33.3333333,
      p50ms: 15.34343,
      count: 200,
      p20ms: 12.1212121,
      p80ms: 26.5555555,
      p95ms: 32.222222,
      p99ms: 33.333333,
      averageMs: 20.23123213,
    },
    {
      groupId: 'POST http://another',
      minMs: 20,
      maxMs: 43.3333333,
      p50ms: 25.34343,
      count: 300,
      p20ms: 22.1212121,
      p80ms: 36.5555555,
      p95ms: 42.222222,
      p99ms: 43.333333,
      averageMs: 30.23123213,
    },
  ];
}
