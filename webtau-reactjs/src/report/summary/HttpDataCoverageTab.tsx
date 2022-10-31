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

import { SortableTable, TableRow, TableValuesRenderer } from '../widgets/SortableTable';

import './HttpDataCoverageTab.css';

export interface HttpOperationCoverage {
  id: string;
  touchedPathsCount: number;
  untouchedPathsCount: number;
  untouchedPercent: number;
  untouchedPaths: string[];
}

interface Props {
  httpCoverage: HttpOperationCoverage[];
}

const header = ["Operation ID", "Touched Paths Count", "Untouched Paths Count", "Untouched Paths (%)"]

const outerTableRenderer: TableValuesRenderer = {
  expandedRowRenderer(row) {
    const untouchedPathsIdx = header.length;
    return <div className="webtau-http-untouched-paths">
      {(row[untouchedPathsIdx] as string[]).map(path => <div className="webtau-http-data-coverage-path">{path}</div>)}
    </div>;
  },
  canBeExpanded() {
    return true;
  },
};

export function HttpDataCoverageTab({httpCoverage}: Props) {
  return (
    <div className="webtau-http-data-coverage">
      <SortableTable
        header={header}
        data={prepareData(httpCoverage)}
        renderer={outerTableRenderer}
        rowKeyProvider={keyProvider}
      />
    </div>
  );
}

function prepareData(httpCoverage: HttpOperationCoverage[]) {
  return httpCoverage.map(coverage => [coverage.id, coverage.touchedPathsCount, coverage.untouchedPathsCount, coverage.untouchedPercent, coverage.untouchedPaths])
}

function keyProvider(row: TableRow): string {
  return row[0] as string;
}

