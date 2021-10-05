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

import moment from 'moment';

import { SelectedIndexes } from '../SelectedIndexes';

import CliBackgroundDetails from './CliBackgroundDetails';

import '../../widgets/Table.css';
import './TestCliBackground.css';
import { WebTauTest } from '../../WebTauTest';
import { CliBackgroundCall } from './CliCalls';

interface Props {
  test: WebTauTest;

  urlState: { [id: string]: string };
  onInternalStateUpdate(newState: { [id: string]: string }): void;
}

interface State {
  selectedIndexes: SelectedIndexes;
}

export default class TestCliBackground extends React.Component<Props, State> {
  state = { selectedIndexes: new SelectedIndexes('') };
  static stateName = 'cliBackgroundIdxs';

  static getDerivedStateFromProps(props: Props) {
    const backgroundIdxs = props.urlState[TestCliBackground.stateName];
    return { selectedIndexes: new SelectedIndexes(backgroundIdxs) };
  }

  render() {
    const { test } = this.props;

    const hasPersonas = test.cliBackground!.some((cliCall) => cliCall.personaId);

    return (
      <table className="test-cli-background-table table">
        <thead>
          <tr>
            <th style={{ width: 35 }} />
            {hasPersonas && <th style={{ width: 50 }}>Persona</th>}
            <th style={{ width: 'auto' }}>Command</th>
            <th style={{ width: 60 }}>Start</th>
          </tr>
        </thead>
        <tbody>
          {test.cliBackground!.map((cliBackground, idx) => this.renderRow(cliBackground, hasPersonas, idx))}
        </tbody>
      </table>
    );
  }

  renderRow(cliBackground: CliBackgroundCall, hasPersonas: boolean, idx: number) {
    const isExpanded = this.isExpanded(idx);

    const className = 'test-cli-background' + (isExpanded ? ' expanded' : '');

    const startDateTime = new Date(cliBackground.startTime);

    const onClick = () => this.onCollapseToggleClick(idx);

    return (
      <React.Fragment key={idx}>
        <tr className={className}>
          <td className="collapse-toggle" onClick={onClick}>
            {isExpanded ? '-' : '+'}
          </td>
          {hasPersonas && (
            <td className="persona" onClick={onClick}>
              {cliBackground.personaId || ''}
            </td>
          )}
          <td className="command" onClick={onClick}>
            {cliBackground.command}
          </td>
          <td className="start" onClick={onClick}>
            {moment(startDateTime).local().format('HH:mm:ss.SSS')}
          </td>
        </tr>

        {isExpanded && <CliBackgroundDetails cliBackground={cliBackground} />}
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
      [TestCliBackground.stateName]: selectedIndexes.toggle(idx),
    });
  };
}
