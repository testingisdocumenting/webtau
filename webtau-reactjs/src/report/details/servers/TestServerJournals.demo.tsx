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

import { Registry, simulateState } from 'react-component-viewer';
import { WebTauTest } from '../../WebTauTest';
import { TestServerJournals } from './TestServerJournals';

const [getUrlState, setUrlState] = simulateState({ cliCallIdxs: '0-1-2' });

export function testServerJournalsDemo(registry: Registry) {
  registry.add('multiple cli calls', () => (
    <TestServerJournals test={createTestWithServers()} urlState={getUrlState()} onInternalStateUpdate={setUrlState} />
  ));
}

function createTestWithServers(): WebTauTest {
  return {
    id: 'testid',
    containerId: '',
    elapsedTime: 0,
    scenario: 'my scenario',
    startTime: 1034343434,
    steps: [],
    servers: [
      {
        serverId: 'echo-server',
        capturedCalls: [
          {
            startTime: 3434343434,
            elapsedTime: 500,
            statusCode: 200,
            url: 'myserver/api',
            method: 'POST',
            requestType: 'application/json',
            responseType: 'application/json',
            capturedRequest: '{"id": "test"}',
            capturedResponse: '{"status": "OK"}',
          },
          {
            startTime: 3434343434,
            elapsedTime: 500,
            statusCode: 200,
            url: 'myserver/api',
            method: 'POST',
            requestType: 'application/json',
            responseType: '',
            capturedRequest: '',
            capturedResponse: 'abc',
          },
        ],
      },
    ],
  };
}
