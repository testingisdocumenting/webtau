/*
 * Copyright 2020 webtau maintainers
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

import './WebTauReport.css';

import { ComponentViewer, Registries, DropDowns } from 'react-component-viewer';
import { sortableTableDemo } from './widgets/SortableTable.demo';
import { webTauReportsDemo } from './WebTauReports.demo';
import { stepsDemo } from './details/steps/Steps.demo';
import { cardWithElapsedTimeDemo } from './widgets/CardWithElapsedTime.demo';
import { cardListDemo } from './widgets/CardList.demo';
import { httpHeaderDemo } from './details/http-header/HttpHeader.demo';
import { testErrorMessageDemo } from './widgets/TestErrorMessage.demo';
import { cliBuildingBlocksDemo } from './details/cli/CliBuildingBlocksDemo';
import { testCliCallsDemo } from './details/cli/TestCliCalls.demo';
import { collapsibleHttpHeaderDemo } from './details/http-header/CollapsibleHttpHeader.demo';
import { testMetadataDemo } from './details/metadata/TestMetadata.demo';
import Loading from './loading/Loading';
import { testSummaryDemo } from './details/TestSummary.demo';
import { testCliBackgroundCallsDemo } from './details/cli/TestCliBackgroundCalls.demo';
import { aggregatedOperationsPerformanceTableDemo } from './perf/AggregatedOperationsPerformanceTable.demo';
import { testServerJournalsDemo } from './details/servers/TestServerJournals.demo';
import { sourceCodeDemo } from './snippet/SourceCode.demo';
import { testsPerformanceDemo } from './summary/TestsPerformance.demo';
import { httpCallsDemo } from './details/http/httpCallsDemo';
import { httpDataCoverageTabDemo } from './summary/HttpDataCoverageTab.demo';
import { allWarningsPanelDemo } from './summary/AllWarningsPanel.demo';

const registries = new Registries();

registries
  .add('widgets')
  .registerAsRows('table', sortableTableDemo)
  .registerAsRows('card with elapsed time', cardWithElapsedTimeDemo)
  .registerAsRows('card list', cardListDemo)
  .registerAsRows('test error message', testErrorMessageDemo)
  .registerAsRows('code snippet', sourceCodeDemo);

registries.add('core').registerAsGrid('steps', 0, stepsDemo).registerAsGrid('test metadata', 0, testMetadataDemo);

registries
  .add('http')
  .registerAsRows('http calls', httpCallsDemo)
  .registerAsRows('http header', httpHeaderDemo)
  .registerAsRows('collapsible http header', collapsibleHttpHeaderDemo)
  .registerAsRows('performance', aggregatedOperationsPerformanceTableDemo);

registries
  .add('cli')
  .registerAsRows('cli building blocks', cliBuildingBlocksDemo)
  .registerAsTabs('cli calls', testCliCallsDemo)
  .registerAsTabs('cli background calls', testCliBackgroundCallsDemo);

registries.add('server').registerAsTabs('server calls', testServerJournalsDemo);

registries
  .add('panels')
  .registerAsTabs('test summary', testSummaryDemo)
  .registerAsTabs('tests performance', testsPerformanceDemo)
  .registerAsTabs('http coverage', httpDataCoverageTabDemo)
  .registerAsTabs('all warnings', allWarningsPanelDemo);

registries
  .add('full reports')
  .registerAsTabs('demo reports', webTauReportsDemo)
  .registerSingle('loading', () => <Loading />);

const dropDowns = new DropDowns();

export function ReportComponentViewer() {
  return <ComponentViewer registries={registries} dropDowns={dropDowns} />;
}
