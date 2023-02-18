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

import { StepToken } from './StepToken';

import { TokenizedMessageToken } from '../../WebTauTest';

import { StyledText } from './StyledText';

import './TokenizedMessage.css';

interface Props {
  message: TokenizedMessageToken[];
}

export function TokenizedMessage({ message }: Props) {
  return (
    <div className="webtau-tokenized-message">
      {message.map((t, idx) => {
        return t.type === 'styledText' ? (
          <StyledText key={idx} lines={t.value} />
        ) : (
          <StepToken key={idx} token={t} next={message[idx + 1]} />
        );
      })}
    </div>
  );
}
