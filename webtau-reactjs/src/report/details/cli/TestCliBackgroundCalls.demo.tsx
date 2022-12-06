/*
 * Copyright 2020 webtau maintainers
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
import TestCliBackground from './TestCliBackground';
import { WebTauTest } from '../../WebTauTest';

const [getUrlState, setUrlState] = simulateState({ cliBackgroundIdxs: '0-1-2' });

export function testCliBackgroundCallsDemo(registry: Registry) {
  registry.add('multiple calls', () => (
    <TestCliBackground
      test={createTestWithCliBackground()}
      urlState={getUrlState()}
      onInternalStateUpdate={setUrlState}
    />
  ));
  registry.add('with personas', () => (
    <TestCliBackground
      test={createTestWithCliBackground('Alice')}
      urlState={getUrlState()}
      onInternalStateUpdate={setUrlState}
    />
  ));
}

function createTestWithCliBackground(personaId?: string): WebTauTest {
  return {
    id: 'testid',
    containerId: '',
    elapsedTime: 0,
    scenario: 'my scenario',
    startTime: 1034343434,
    steps: [],
    warnings: [],
    cliBackground: [
      {
        command: 'ls -l',
        out: 'line 1\nline 2\nline 3',
        err: 'line 4\nline 5',
        startTime: 0,
        config: {
          'workding dir': '/home/user/working/dir',
          $VAR: 'my_var_value',
          timeout: 2000,
        },
      },
      {
        personaId,
        command: 'ls2 -l',
        out: 'line 1\nline 2\nline 3',
        err: 'line 4\nline 5',
        startTime: 165210,
        config: {},
      },
    ],
  };
}
