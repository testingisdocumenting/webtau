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

import { Registry, simpleAction } from 'react-component-viewer';
import { AllWarningsPanel } from './AllWarningsPanel';
import { WebTauWarning } from '../WebTauTest';

function onTestSelect(id: string) {
  simpleAction('clicked ' + id)();
}

export function allWarningsPanelDemo(registry: Registry) {
  registry.add('multiple tests', () => <AllWarningsPanel warnings={sampleWarnings()} onSwitchToTest={onTestSelect} />);
}

export function sampleWarnings(): WebTauWarning[] {
  return [
    {
      testId: 'test1',
      scenario: 'my test',
      shortContainerId: 'container one',
      message: 'warning one',
      input: {},
    },
    {
      testId: 'test2',
      scenario: 'your test',
      shortContainerId: 'container one',
      message: 'warning two',
      input: { k1: 'v1', k2: 'v2' },
    },
    {
      testId: 'test2',
      scenario: 'their test',
      shortContainerId: 'container two',
      message: 'warning three',
      input: { k3: 'v3', k4: 'v4' },
    },
  ];
}
