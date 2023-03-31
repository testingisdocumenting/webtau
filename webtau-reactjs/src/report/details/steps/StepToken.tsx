/*
 * Copyright 2023 webtau maintainers
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

import { TokenizedMessageToken } from '../../WebTauTest';

import './StepToken.css';

interface Props {
  token: TokenizedMessageToken;
  next?: TokenizedMessageToken;
}

export function StepToken({ token, next }: Props) {
  const className = 'step-token ' + token.type;
  const isNextDelimiter = next?.type === 'delimiter' || next?.type === 'delimiterNoAutoSpacing';
  const isCurrentNoAutoSpacing = token.type == 'delimiterNoAutoSpacing';

  const valueToUse = token.value + (isNextDelimiter || isCurrentNoAutoSpacing ? '' : ' ');

  switch (token.type) {
    case 'url':
      return (
        <a className="step-token url" href={token.value} target="_blank" rel="noopener noreferrer">
          {valueToUse}
        </a>
      );
    default:
      return <span className={className}>{valueToUse}</span>;
  }
}
