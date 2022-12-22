/*
 * Copyright 2022 webtau maintainers
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

import { NavigationEntryStatus } from './NavigationEntryStatus';

import './NavigationEntry.css';

interface Props {
  label: string;
  status: string;
  isSelected: boolean;
  withWarning?: boolean;
  onSelect(): void;
}

export function NavigationEntry({ label, status, isSelected, withWarning, onSelect }: Props) {
  const className = 'navigation-entry' + (isSelected ? ' selected' : '');
  return (
    <div className={className} onClick={onSelect}>
      <div className="label">
        {withWarning && <span className="warning-sign">!</span>}
        {label}
      </div>
      <NavigationEntryStatus status={status} />
    </div>
  );
}
