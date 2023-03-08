/*
 * Copyright 2020 webtau maintainers
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

import React, { Component } from 'react';

import TestErrorMessage from '../../widgets/TestErrorMessage';

import HttpPayload from './HttpPayload';
import ElapsedTime from '../../widgets/ElapsedTime';

import moment from 'moment';

import HttpCallHeaders from './HttpCallHeaders';
import { SelectedIndexes } from '../SelectedIndexes';

import './TestHttpCalls.css';
import '../../widgets/Table.css';
import { TokenizedMessage } from '../steps/TokenizedMessage';

class TestHttpCalls extends Component {
  state = {};

  static stateName = 'httpCallIdxs';

  static getDerivedStateFromProps(props) {
    const callIdxs = props.urlState[TestHttpCalls.stateName];
    return { selectedIndexes: new SelectedIndexes(callIdxs) };
  }

  render() {
    const { test } = this.props;

    const hasPersonas = test.httpCalls.some((httpCall) => httpCall.personaId);

    return (
      <div className="http">
        <table className="http-table table">
          <thead>
            <tr>
              <th width="35px" />
              {hasPersonas && <th width="50px">Persona</th>}
              <th width="60px">Method</th>
              <th width="40px">Code</th>
              <th width="60px">Start</th>
              <th width="60px">Took</th>
              <th>Url</th>
            </tr>
          </thead>
          <tbody>{test.httpCalls.map((httpCall, idx) => this.renderRow(httpCall, hasPersonas, idx))}</tbody>
        </table>
      </div>
    );
  }

  renderRow(httpCall, hasPersonas, idx) {
    const { reportNavigation } = this.props;

    const hasProblem = httpCall.mismatches.length > 0 || !!httpCall.errorMessage;
    const isExpanded = this.isExpanded(idx);

    const className = 'test-http-call' + (hasProblem ? ' with-problem' : '') + (isExpanded ? ' expanded' : '');

    const startDateTime = new Date(httpCall.startTime);

    const onClick = () => this.onCollapseToggleClick(idx);

    return (
      <React.Fragment key={idx}>
        <tr className={className}>
          <td className="collapse-toggle" onClick={onClick}>
            {isExpanded ? '-' : '+'}
          </td>
          {hasPersonas && (
            <td className="persona" onClick={onClick}>
              {httpCall.personaId}
            </td>
          )}
          <td className="webtau-method-cell" onClick={onClick}>
            {httpCall.method}
          </td>
          <td className="webtau-status-code-cell" onClick={onClick}>
            {httpCall.responseStatusCode}
          </td>
          <td onClick={onClick}>{moment(startDateTime).local().format('HH:mm:ss.SSS')}</td>
          <td className="http-call-elapsed-time" onClick={onClick}>
            <ElapsedTime millis={httpCall.elapsedTime} />
          </td>
          <td className="webtau-url-cell">{httpCall.url}</td>
        </tr>

        {isExpanded && <HttpCallDetails httpCall={httpCall} reportNavigation={reportNavigation} />}
      </React.Fragment>
    );
  }

  isExpanded(idx) {
    return this.state.selectedIndexes.isExpanded(idx);
  }

  onCollapseToggleClick = (idx) => {
    const { onInternalStateUpdate } = this.props;
    const { selectedIndexes } = this.state;

    onInternalStateUpdate({
      [TestHttpCalls.stateName]: selectedIndexes.toggle(idx),
    });
  };
}

function HttpCallDetails({ httpCall, reportNavigation }) {
  const colSpanAll = 10000; // arbitrary large number to span all
  return (
    <tr className="test-http-call-details">
      <td />
      <td colSpan={colSpanAll}>
        <Mismatches httpCall={httpCall} />
        <ErrorMessage httpCall={httpCall} />

        <div className="http-call-header-body">
          <HttpCallHeaders request={httpCall.requestHeader} response={httpCall.responseHeader} />

          <div className="body-request-response">
            <Request httpCall={httpCall} reportNavigation={reportNavigation} />
            <Response httpCall={httpCall} reportNavigation={reportNavigation} />
          </div>
        </div>
      </td>
    </tr>
  );
}

function Mismatches({ httpCall }) {
  return httpCall.mismatches.map((m, idx) => (
    <pre key={idx} className="webtau-http-mismatch">
      <TokenizedMessage message={m} removeLastErrorToken={false} />
    </pre>
  ));
}

function ErrorMessage({ httpCall }) {
  if (!httpCall.errorMessage) {
    return null;
  }

  return (
    <div className="test-http-calls-call-error-message">
      <TestErrorMessage message={httpCall.errorMessage} />
    </div>
  );
}

function Request({ httpCall, reportNavigation }) {
  if (!httpCall.requestBody) {
    return <div />;
  }

  return (
    <div className="request">
      <HttpPayload
        caption="Request"
        type={httpCall.requestType}
        data={httpCall.requestBody}
        httpCallId={httpCall.id}
        payloadType="request"
        onZoom={reportNavigation.zoomInHttpPayload}
      />
    </div>
  );
}

function Response({ httpCall, reportNavigation }) {
  if (!httpCall.responseBody) {
    return null;
  }

  return (
    <div className="response">
      <HttpPayload
        caption="Response"
        type={httpCall.responseType}
        data={httpCall.responseBody}
        checks={httpCall.responseBodyChecks}
        httpCallId={httpCall.id}
        payloadType="response"
        onZoom={reportNavigation.zoomInHttpPayload}
      />
    </div>
  );
}

export default TestHttpCalls;
