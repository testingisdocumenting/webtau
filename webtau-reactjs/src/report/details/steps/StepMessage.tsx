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

import './StepMessage.css';

interface Props {
  message: TokenizedMessageToken[];
  removeLastErrorToken: boolean;
}

export function StepMessage({ message, removeLastErrorToken }: Props) {
  const modifiedMessageTokens = modifiedMessage();

  return (
    <div className="message">
      {modifiedMessageTokens.map((t, idx) => (
        <StepToken key={idx} token={t} next={modifiedMessageTokens[idx + 1]} />
      ))}
    </div>
  );

  function modifiedMessage() {
    if (message.length === 0) {
      return message;
    }

    if (!removeLastErrorToken) {
      return message;
    }

    const lastToken = message[message.length - 1];
    return removeLastErrorToken && lastToken.type === 'error' ? message.slice(0, message.length - 1) : message;
  }
}
