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
import { CollapsibleHttpHeader } from './CollapsibleHttpHeader';
import { Registry, simulateState } from 'react-component-viewer';
const [getCollapsed, setCollapsed] = simulateState(true);

export function collapsibleHttpHeaderDemo(registry: Registry) {
  registry.add('with header information present', () => (
    <div style={{ width: 400 }}>
      <CollapsibleHttpHeader
        label="request header"
        header={header()}
        isCollapsed={getCollapsed()}
        onToggle={toggleCollapsed}
      />
    </div>
  ));
  registry.add('without header information', () => (
    <CollapsibleHttpHeader label="request header" isCollapsed={getCollapsed()} onToggle={toggleCollapsed} />
  ));
}

function toggleCollapsed() {
  setCollapsed(false);
}

function header() {
  return [
    { key: 'secret', value: '********' },
    { key: 'X-something', value: 'value' },
  ];
}
