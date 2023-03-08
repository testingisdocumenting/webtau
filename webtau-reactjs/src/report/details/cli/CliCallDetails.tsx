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

import CliOutputCard from './CliOutputCard';

import TestErrorMessage from '../../widgets/TestErrorMessage';

import { CliForegroundCall } from './CliCalls';

import { KeyValueGrid } from '../../widgets/KeyValueGrid';

import './CliCallDetails.css';
import { TokenizedMessage } from '../steps/TokenizedMessage';

interface Props {
  cliCall: CliForegroundCall;
}

export default function CliCallDetails({ cliCall }: Props) {
  const colSpanAll = 10000; // arbitrary large number to span all

  return (
    <tr className="cli-command-details">
      <td />
      <td colSpan={colSpanAll}>
        <Mismatches cliCall={cliCall} />
        <ErrorMessage cliCall={cliCall} />

        <KeyValueGrid data={cliCall.config} />
        <CliOutputCard classifier="standard" output={cliCall.out} matchedLines={cliCall.outMatches} />
        <CliOutputCard classifier="error" output={cliCall.err} matchedLines={cliCall.errMatches} />
      </td>
    </tr>
  );
}

function Mismatches({ cliCall }: Props) {
  return (
    <>
      {cliCall.mismatches.map((m, idx) => (
        <div key={idx} className="webtau-cli-mismatch">
          <TokenizedMessage message={m} />
        </div>
      ))}
    </>
  );
}

function ErrorMessage({ cliCall }: Props) {
  if (!cliCall.errorMessage) {
    return null;
  }

  return (
    <div className="cli-call-details-error-message">
      <TestErrorMessage message={cliCall.errorMessage} />
    </div>
  );
}
