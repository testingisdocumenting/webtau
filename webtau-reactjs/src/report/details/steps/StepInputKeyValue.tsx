/*
 * Copyright 2021 webtau maintainers
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

import { WebTauStepInputKeyValue } from '../../WebTauTest';
import './StepInputKeyValue.css';

interface Props {
  data: WebTauStepInputKeyValue;
}

export function StepInputKeyValue({ data }: Props) {
  return (
    <div className="webtau-step-input-key-value">
      {Object.keys(data).map((key) => {
        const value = data[key];
        return (
          <React.Fragment key={key}>
            <div className="webtau-step-input-key">{key}</div>
            <div className={'webtau-step-input-value ' + valueClassName(value)}>{value}</div>
          </React.Fragment>
        );
      })}
    </div>
  );
}

function valueClassName(value: any) {
  if (typeof value === 'string') {
    return 'webtau-value-string';
  }

  return 'webtau-value-number';
}
