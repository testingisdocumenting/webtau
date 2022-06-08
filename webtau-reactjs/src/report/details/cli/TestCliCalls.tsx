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

import moment from 'moment';

import ElapsedTime from '../../widgets/ElapsedTime';
import CliCallDetails from './CliCallDetails';
import { SelectedIndexes } from '../SelectedIndexes';

import '../../widgets/Table.css';
import './TestCliCalls.css';

import { WebTauTest } from '../../WebTauTest';
import { CliForegroundCall } from './CliCalls';

interface Props {
  test: WebTauTest;

  urlState: { [id: string]: string };
  onInternalStateUpdate(newState: { [id: string]: string }): void;
}

interface State {
  selectedIndexes: SelectedIndexes;
}

export default class TestCliCalls extends React.Component<Props, State> {
  state = { selectedIndexes: new SelectedIndexes('') };
  static stateName = 'cliCallIdxs';

  static getDerivedStateFromProps(props: any) {
    const callIdxs = props.urlState[TestCliCalls.stateName];
    return { selectedIndexes: new SelectedIndexes(callIdxs) };
  }

  render() {
    const { test } = this.props;

    const hasPersonas = test.cliCalls!.some((cliCall) => cliCall.personaId);

    return (
      <table className="test-cli-calls-table table">
        <thead>
          <tr>
            <th style={{ width: 35 }} />
            {hasPersonas && <th style={{ width: 50 }}>Persona</th>}
            <th style={{ width: 'auto' }}>Command</th>
            <th style={{ width: 80 }}>Exit Code</th>
            <th style={{ width: 60 }}>Start</th>
            <th style={{ width: 60 }}>Took</th>
          </tr>
        </thead>
        <tbody>{test.cliCalls!.map((cliCall, idx) => this.renderRow(cliCall, hasPersonas, idx))}</tbody>
      </table>
    );
  }

  renderRow(cliCall: CliForegroundCall, hasPersonas: boolean, idx: number) {
    const isExpanded = this.isExpanded(idx);

    const hasProblem = cliCall.mismatches.length > 0 || !!cliCall.errorMessage;

    const className = 'test-cli-call' + (hasProblem ? ' with-problem' : '') + (isExpanded ? ' expanded' : '');

    const startDateTime = new Date(cliCall.startTime);

    const onClick = () => this.onCollapseToggleClick(idx);

    return (
      <React.Fragment key={idx}>
        <tr className={className}>
          <td className="collapse-toggle" onClick={onClick}>
            {isExpanded ? '-' : '+'}
          </td>
          {hasPersonas && (
            <td className="persona" onClick={onClick}>
              {cliCall.personaId || ''}
            </td>
          )}
          <td className="command" onClick={onClick}>
            {cliCall.command}
          </td>
          <td className="exit-code" onClick={onClick}>
            {cliCall.exitCode}
          </td>
          <td className="start" onClick={onClick}>
            {moment(startDateTime).local().format('HH:mm:ss.SSS')}
          </td>
          <td className="cli-call-elapsed-time" onClick={onClick}>
            <ElapsedTime millis={cliCall.elapsedTime} />
          </td>
        </tr>

        {isExpanded && <CliCallDetails cliCall={cliCall} />}
      </React.Fragment>
    );
  }

  isExpanded(idx: number) {
    return this.state.selectedIndexes.isExpanded(idx);
  }

  onCollapseToggleClick = (idx: number) => {
    const { onInternalStateUpdate } = this.props;
    const { selectedIndexes } = this.state;

    onInternalStateUpdate({
      [TestCliCalls.stateName]: selectedIndexes.toggle(idx),
    });
  };
}
