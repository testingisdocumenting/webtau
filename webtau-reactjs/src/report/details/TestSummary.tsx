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
import { SourceCode } from '../snippet/SourceCode';

import { CardLabelAndNumber } from '../widgets/CardLabelAndNumber';
import NumberOfHttpCalls from '../dashboard/NumberOfHttpCalls';

import Report from '../Report';

import { Card } from '../widgets/Card';
import { CardWithTime } from '../widgets/CardWithTime';

import CardWithElapsedTime from '../widgets/CardWithElapsedTime';

import TestErrorMessage from '../widgets/TestErrorMessage';

import { TestMetadata } from './metadata/TestMetadata';
import { WebTauTest } from '../WebTauTest';

import CardList from '../widgets/CardList';
import { TestName } from './TestName';
import { WebTauWarningMessage } from './WebTauWarningMessage';

import './TestSummary.css';

interface TestProps {
  test: WebTauTest;
}

export function TestSummary({ test }: TestProps) {
  const numberOfHttpCalls = test.httpCalls ? test.httpCalls.length : 0;

  return (
    <div className="webtau-test-summary">
      <div className="webtau-test-summary-cards">
        <TestWarnings test={test} />

        <CardList label={<TestName test={test} />}>
          <CardWithTime label="Start Time (Local)" time={test.startTime} />

          <CardWithTime label="Start Time (UTC)" utc={true} time={test.startTime} />

          <CardWithElapsedTime label="Execution time" millis={test.elapsedTime} />
        </CardList>

        <TestMetadata metadata={test.metadata} />

        {numberOfHttpCalls > 0 ? (
          <CardList label="HTTP Summary">
            <NumberOfHttpCalls number={numberOfHttpCalls} />
            <AverageHttpCallsTime test={test} />
            <OverallHttpCallsTime test={test} />
          </CardList>
        ) : null}

        <OptionalPreBlock className="context-description" message={test.contextDescription} />
        <CardPreMessage message={test.exceptionMessage} />
      </div>

      {test.failedCodeSnippets && test.failedCodeSnippets.map((cs, idx) => <SourceCode key={idx} {...cs} />)}
    </div>
  );
}

function TestWarnings({ test }: TestProps) {
  const warnings = test.warnings;
  if (warnings.length === 0) {
    return null;
  }

  return (
    <Card className="webtau-test-warning" warning={true}>
      {warnings.map((warning, idx) => (
        <WebTauWarningMessage key={idx} message={warning.message} input={warning.input} />
      ))}
    </Card>
  );
}

function OverallHttpCallsTime({ test }: TestProps) {
  if (!test.httpCalls) {
    return null;
  }

  return <CardLabelAndNumber label="Overall Time (ms)" number={Report.overallHttpCallTimeForTest(test)} />;
}

function AverageHttpCallsTime({ test }: TestProps) {
  if (!test.httpCalls) {
    return null;
  }

  return <CardLabelAndNumber label="Average Time (ms)" number={Report.averageHttpCallTimeForTest(test).toFixed(2)} />;
}

function CardPreMessage({ message }: { message?: string }) {
  if (!message) {
    return null;
  }

  return (
    <Card className="card-pre-message">
      <TestErrorMessage message={message.trim()} />
    </Card>
  );
}

function OptionalPreBlock({ className, message }: { className: string; message?: string }) {
  if (!message) {
    return null;
  }

  return (
    <div className={className}>
      <pre>{message}</pre>
    </div>
  );
}
