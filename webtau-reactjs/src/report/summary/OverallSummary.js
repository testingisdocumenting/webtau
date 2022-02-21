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

import TabSelection from '../widgets/TabSelection';

import { ConfigTable } from './ConfigTable';
import OverallInfo from './OverallInfo';
import OverallHttpPerformance from './OverallHttpPerformance';

import { AggregatedOperationsPerformanceTable } from '../perf/AggregatedOperationsPerformanceTable';

import { EnvVarsTable } from './EnvVarsTable';

import { TestsPerformance } from './TestsPerformance';

import './OverallSummary.css';

const summaryTabName = 'Summary';
const configurationTabName = 'Configuration';
const envVarsTabName = 'Environment Variables';
const testsPerformanceTabName = 'Tests Performance';
const overallHttpPerformanceTabName = 'Overall HTTP Performance';
const httpOperationsPerformanceTabName = 'HTTP Operations Performance';

export default class OverallSummary extends React.Component {
  constructor(props) {
    super(props);
    this.tabNames = availableTabNames(props.report);
  }

  render() {
    const { onTabSelection, selectedTabName = this.tabNames[0] } = this.props;

    return (
      <div className="overall-summary">
        <TabSelection tabs={this.tabNames} selectedTabName={selectedTabName} onTabSelection={onTabSelection} />

        <div className="overall-summary-tab-content">{this.renderTabContent()}</div>
      </div>
    );
  }

  renderTabContent() {
    const { selectedTabName = this.tabNames[0], report, onSwitchToHttpCalls, onSwitchToSkippedHttpCalls } = this.props;

    if (selectedTabName === summaryTabName) {
      return (
        <OverallInfo
          report={report}
          onSwitchToHttpCalls={onSwitchToHttpCalls}
          onSwitchToSkippedHttpCalls={onSwitchToSkippedHttpCalls}
        />
      );
    } else if (selectedTabName === testsPerformanceTabName) {
      return <TestsPerformance tests={report.tests} />;
    } else if (selectedTabName === overallHttpPerformanceTabName) {
      return <OverallHttpPerformance report={report} />;
    } else if (selectedTabName === httpOperationsPerformanceTabName) {
      return <AggregatedOperationsPerformanceTable operations={report.httpPerformance.aggregated} />;
    } else if (selectedTabName === configurationTabName) {
      return <ConfigTable report={report} />;
    } else if (selectedTabName === envVarsTabName) {
      return <EnvVarsTable report={report} />;
    } else {
      return null;
    }
  }
}

function availableTabNames(report) {
  const tabNames = [summaryTabName, configurationTabName, envVarsTabName, testsPerformanceTabName];
  if (report.hasHttpCalls()) {
    tabNames.push(overallHttpPerformanceTabName);
  }

  if (report.httpPerformance && report.httpPerformance.aggregated.length > 0) {
    tabNames.push(httpOperationsPerformanceTabName);
  }

  return tabNames;
}
