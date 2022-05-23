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

import HttpPayload from './HttpPayload';
import { TestName } from '../TestName';

import { Card } from '../../widgets/Card';
import { CardLabelAndNumber } from '../../widgets/CardLabelAndNumber';
import { CardWithTime } from '../../widgets/CardWithTime';

import HttpCallHeaders from './HttpCallHeaders';

import { WebTauReportNavigation } from '../../WebTauReportNavigation';
import { HttpCall } from '../../WebTauTest';
import CardList from '../../widgets/CardList';

import './HttpCallDetails.css';

interface HttpCallProps {
  httpCall: HttpCall;
}

interface Props extends HttpCallProps {
  reportNavigation: WebTauReportNavigation;
}

export function HttpCallDetails({ httpCall, reportNavigation }: Props) {
  if (!httpCall.test) {
    return <HttpCallSkippedDetails httpCall={httpCall} />;
  }

  return (
    <div className="http-call-details">
      <CardList>
        <UrlAndTestNameCard httpCall={httpCall} reportNavigation={reportNavigation} />
        <CardWithTime label="Start Time (Local)" time={httpCall.startTime} />
        <CardWithTime label="Start Time (UTC)" utc={true} time={httpCall.startTime} />
        <CardLabelAndNumber label="Latency (ms)" number={httpCall.elapsedTime} />
      </CardList>

      <Mismatches httpCall={httpCall} />
      <ErrorMessage httpCall={httpCall} />

      <HttpCallHeaders useCards="true" request={httpCall.requestHeader} response={httpCall.responseHeader} />

      <div className="body-request-response">
        <Request httpCall={httpCall} reportNavigation={reportNavigation} />
        <Response httpCall={httpCall} reportNavigation={reportNavigation} />
      </div>
    </div>
  );
}

function HttpCallSkippedDetails({ httpCall }: HttpCallProps) {
  return (
    <div className="http-call-details">
      <Card className="http-call-no-details">{httpCall.label} was not exercised</Card>
    </div>
  );
}

function Mismatches({ httpCall }: HttpCallProps) {
  if (httpCall.mismatches.length === 0) {
    return null;
  }

  const mismatches = httpCall.mismatches.map((m, idx) => (
    <div key={idx} className="mismatch">
      <pre>{m}</pre>
    </div>
  ));

  return <Card className="http-call-details-mismatches">{mismatches}</Card>;
}

function ErrorMessage({ httpCall }: HttpCallProps) {
  if (!httpCall.errorMessage) {
    return null;
  }

  return <Card className="http-call-details-error-message">{httpCall.errorMessage}</Card>;
}

function Request({ httpCall, reportNavigation }: Props) {
  if (!httpCall.requestBody) {
    return <div />;
  }

  return (
    <Card className="http-call-details-request-details">
      <HttpPayload
        caption="Request"
        type={httpCall.requestType}
        data={httpCall.requestBody}
        httpCallId={httpCall.id}
        payloadType="request"
        checks={httpCall.responseBodyChecks}
        onZoom={reportNavigation.zoomInHttpPayload}
      />
    </Card>
  );
}

function Response({ httpCall, reportNavigation }: Props) {
  if (!httpCall.responseBody) {
    return <div />;
  }

  return (
    <Card className="http-call-details-response-details">
      <HttpPayload
        caption="Response"
        type={httpCall.responseType}
        data={httpCall.responseBody}
        checks={httpCall.responseBodyChecks}
        httpCallId={httpCall.id}
        payloadType="response"
        onZoom={reportNavigation.zoomInHttpPayload}
      />
    </Card>
  );
}

function UrlAndTestNameCard({ httpCall, reportNavigation }: Props) {
  return (
    <Card>
      <Url httpCall={httpCall} />
      <TestName
        className="http-call-details-url-and-test-name-card-test-name"
        test={httpCall.test!}
        onTestClick={reportNavigation.selectTest}
      />
    </Card>
  );
}

function Url({ httpCall }: HttpCallProps) {
  return (
    <div className="http-call-details-url">
      <div className="method">{httpCall.method}</div>
      <div className="url">
        <a href={httpCall.url} target="_blank" rel="noreferrer">
          {httpCall.url}
        </a>
      </div>
    </div>
  );
}
