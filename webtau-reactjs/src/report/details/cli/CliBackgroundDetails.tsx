/*
 * Copyright 2020 webtau maintainers
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

import './CliCallDetails.css';
import { CliBackgroundCall } from './CliForegroundCall';
import { KeyValueGrid } from '../../widgets/KeyValueGrid';

interface Props {
  cliBackground: CliBackgroundCall;
}
export default function CliBackgroundDetails({ cliBackground }: Props) {
  return (
    <tr className="cli-command-details">
      <td />
      <td colSpan={2}>
        <KeyValueGrid data={cliBackground.config} />
        <CliOutputCard classifier="standard" output={cliBackground.out} matchedLines={[]} />
        <CliOutputCard classifier="error" output={cliBackground.err} matchedLines={[]} />
      </td>
    </tr>
  );
}
