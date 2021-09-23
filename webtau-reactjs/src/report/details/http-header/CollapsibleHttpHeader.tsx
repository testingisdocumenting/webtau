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
import HttpHeader from './HttpHeader';
import { StringKeyValue } from '../../WebTauTest';

interface Props {
  label: string;
  header?: StringKeyValue[];
  isCollapsed: boolean;
  onToggle(): void;
}

export function CollapsibleHttpHeader({ label, header, isCollapsed, onToggle }: Props) {
  if (!header) {
    return (
      <div className="empty-http-header" onClick={onToggle}>
        empty header
      </div>
    );
  }

  if (isCollapsed) {
    return (
      <div className="collapsed-http-header" onClick={onToggle}>
        {label} (click to expand)
      </div>
    );
  }

  return (
    <div className="uncollapsed-http-header">
      <div className="http-header-label" onClick={onToggle}>
        {label}
      </div>
      <HttpHeader header={header} />
    </div>
  );
}
