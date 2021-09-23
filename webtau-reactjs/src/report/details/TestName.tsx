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

import './TestName.css';

import { WebTauTest } from '../WebTauTest';

interface Props {
  test: WebTauTest;
  onTestClick?(id: string): void;
  className?: string;
}

export function TestName({ test, onTestClick, className }: Props) {
  const onClick = onTestClick ? () => onTestClick(test.id) : undefined;
  const fullClassName = 'test-name' + (className ? ' ' + className : '') + (onClick ? ' clickable' : '');

  return (
    <div className={fullClassName} onClick={onClick}>
      <div className="container-id">{test.containerId}</div>

      <div className="scenario">{test.scenario}</div>
    </div>
  );
}
