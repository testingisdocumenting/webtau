/*
 * Copyright 2021 webtau maintainers
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

import { WebTauServerCapturedCall, WebTauTest } from '../../WebTauTest';
import { SelectedIndexes } from '../SelectedIndexes';
import moment from 'moment';
import ElapsedTime from '../../widgets/ElapsedTime';

import './TestServerJournals.css';
import HttpPayload from '../http/HttpPayload';

export const serverJournalsIdxStateName = 'serverCallIdxs';

interface Props {
  test: WebTauTest;

  urlState: { [id: string]: string };
  onInternalStateUpdate(newState: { [id: string]: string }): void;
}

interface JournalEntry extends WebTauServerCapturedCall {
  serverId: string;
}

export function TestServerJournals({ test, urlState, onInternalStateUpdate }: Props) {
  const callIdxs = urlState[serverJournalsIdxStateName];
  const selectedIndexes = new SelectedIndexes(callIdxs);

  const journalEntries = buildJournalEntries();

  return (
    <div className="webtau-server-journals">
      <table className="webtau-server-journals-table table">
        <thead>
          <tr>
            <th style={{ width: 35 }} />
            <th style={{ width: 100 }}>Server Id</th>
            <th style={{ width: 40 }}>Method</th>
            <th style={{ width: 40 }}>Code</th>
            <th style={{ width: 60 }}>Start</th>
            <th style={{ width: 60 }}>Took</th>
            <th style={{ width: 'auto' }}>URL</th>
          </tr>
        </thead>
        <tbody>
          {journalEntries.map((entry, idx) => (
            <Row
              idx={idx}
              journalEntry={entry}
              onClick={onCollapseToggleClick}
              hasProblem={false}
              isExpanded={selectedIndexes.isExpanded(idx)}
            />
          ))}
        </tbody>
      </table>
    </div>
  );

  function buildJournalEntries(): JournalEntry[] {
    const result: JournalEntry[] = [];

    test.servers!.forEach((server) => {
      server.capturedCalls.forEach((call) => {
        result.push({
          serverId: server.serverId,
          ...call,
        });
      });
    });

    return result;
  }

  function onCollapseToggleClick(idx: number) {
    onInternalStateUpdate({
      [serverJournalsIdxStateName]: selectedIndexes.toggle(idx),
    });
  }
}

interface RowProps {
  idx: number;
  journalEntry: JournalEntry;
  hasProblem: boolean;
  isExpanded: boolean;
  onClick(idx: number): void;
}

function Row({ idx, journalEntry, hasProblem, isExpanded, onClick }: RowProps) {
  const startDateTime = new Date(journalEntry.startTime);

  const className =
    'webtau-server-journal-entry' + (hasProblem ? ' with-problem' : '') + (isExpanded ? ' expanded' : '');

  const onClickWithIdx = () => onClick(idx);

  const colSpanAll = 10000; // arbitrary large number to span all

  return (
    <React.Fragment key={idx}>
      <tr className={className}>
        <td className="collapse-toggle" onClick={onClickWithIdx}>
          {isExpanded ? '-' : '+'}
        </td>
        <td onClick={onClickWithIdx} style={{ whiteSpace: 'nowrap' }}>
          {journalEntry.serverId}
        </td>
        <td className="webtau-method-cell" onClick={onClickWithIdx}>
          {journalEntry.method}
        </td>
        <td className="webtau-status-code-cell" onClick={onClickWithIdx}>
          {journalEntry.statusCode}
        </td>
        <td onClick={onClickWithIdx}>{moment(startDateTime).local().format('HH:mm:ss.SSS')}</td>
        <td onClick={onClickWithIdx}>
          <ElapsedTime millis={journalEntry.elapsedTime} />
        </td>
        <td className="webtau-url-cell">{journalEntry.url}</td>
      </tr>

      {isExpanded && (
        <tr className="webtau-server-journal-entry-details">
          <td />
          <td colSpan={colSpanAll}>
            <div className="body-request-response">
              <HttpPayload
                caption="Request"
                type={journalEntry.requestType}
                data={journalEntry.capturedRequest}
                checks={[]}
                httpCallId={'NA'}
                payloadType="request"
                onZoom={undefined}
              />
              <HttpPayload
                caption="Response"
                type={journalEntry.responseType}
                data={journalEntry.capturedResponse}
                checks={[]}
                httpCallId={'NA'}
                payloadType="response"
                onZoom={undefined}
              />
            </div>
          </td>
        </tr>
      )}
    </React.Fragment>
  );
}
