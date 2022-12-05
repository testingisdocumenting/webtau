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

import React, { useState } from 'react';

import './AllWarningsPanel.css';
import Card from '../widgets/Card';

export interface WebTauWarning {
  testId: string;
  message: string;
  input: object;
}

interface Props {
  warnings: WebTauWarning[];
}

export function AllWarningsPanel({ warnings }: Props) {
  const [isCollapsed, setIsCollapsed] = useState(true);

  if (warnings.length === 0) {
    return null;
  }

  if (isCollapsed) {
    return (
      <Card className="webtau-all-warnings-panel collapsed" warning={true}>
        There are {warnings.length} warning(s). Click to expand
      </Card>
    );
  }

  return <div className="webtau-all-warnings-panel expanded"></div>;
}
