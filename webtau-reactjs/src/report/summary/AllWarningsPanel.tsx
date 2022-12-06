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

import React, { useState } from 'react';

import Card from '../widgets/Card';
import { SortableTable, TableValuesRenderer } from '../widgets/SortableTable';

import { WebTauWarningMessage } from '../details/WebTauWarningMessage';

import { WebTauWarning } from '../WebTauTest';

import './AllWarningsPanel.css';

interface Props {
  warnings: WebTauWarning[];

  onSwitchToTest(testId: string): void;
}

const warningsTableHeader = ['Warning', 'Test'];

const warningsTableRenderer: TableValuesRenderer = {
  cellRenderer(columnName: string, value: any): JSX.Element {
    if (columnName === 'Test') {
      return (
        <div className="webtau-warning-test-url" onClick={() => value.onSwitchToTest(value.id)}>
          {value.label}
        </div>
      );
    } else if (columnName === 'Warning') {
      return <WebTauWarningMessage message={value.message} input={value.input} />;
    }

    return <>{value}</>;
  },
};

export function AllWarningsPanel({ warnings, onSwitchToTest }: Props) {
  const [isCollapsed, setIsCollapsed] = useState(true);

  if (!warnings || warnings.length === 0) {
    return null;
  }

  if (isCollapsed) {
    return (
      <Card className="webtau-all-warnings-panel collapsed" warning={true} onClick={expandWarnings}>
        There are {warnings.length} warning(s). Click to expand
      </Card>
    );
  }

  return (
    <div className="webtau-all-warnings-panel expanded">
      <SortableTable
        header={warningsTableHeader}
        data={generateWarningsData(warnings, onSwitchToTest)}
        renderer={warningsTableRenderer}
      />
    </div>
  );

  function expandWarnings() {
    setIsCollapsed(false);
  }
}

function generateWarningsData(warnings: WebTauWarning[], onSwitchToTest: (id: string) => void) {
  return warnings.map((warning) => {
    const test = {
      label: warning.shortContainerId + ' -> ' + warning.scenario,
      id: warning.testId,
      onSwitchToTest,
    };

    const message = {
      message: warning.message,
      input: warning.input,
    };

    return [message, test];
  });
}
