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

import './KeyValueGrid.css';

interface Props {
  data: { [key: string]: string | number };
  parentClassName?: string;
  keyClassName?: string;
  valueClassName?: string;
}

export function KeyValueGrid({
  data,
  parentClassName = 'webtau-key-value-grid',
  keyClassName = 'webtau-key-value-grid-key',
  valueClassName = 'webtau-key-value-grid-value',
}: Props) {
  return (
    <div className={parentClassName}>
      {Object.keys(data).map((key) => {
        const value = data[key];
        return (
          <React.Fragment key={key}>
            <div className={keyClassName}>{key}</div>
            <div className={valueClassName + ' ' + valueClassifierClassName(value)}>{renderValue(value)}</div>
          </React.Fragment>
        );
      })}
    </div>
  );
}

function valueClassifierClassName(value: any) {
  if (typeof value === 'string') {
    return 'string';
  }

  return 'number';
}

function renderValue(value: any) {
  if (typeof value === 'string' || typeof value === 'number') {
    return value;
  }

  return JSON.stringify(value);
}
