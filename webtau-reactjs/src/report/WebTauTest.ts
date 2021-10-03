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

import { CliBackgroundCall, CliForegroundCall } from './details/cli/CliCalls';

export interface WebTauTest {
  id: string;
  containerId: string;
  scenario: string;
  steps: WebTauStep[];
  httpCalls?: HttpCall[];
  cliCalls?: CliForegroundCall[];
  cliBackground?: CliBackgroundCall[];
  metadata?: { [key: string]: string };
  startTime: number;
  elapsedTime: number;
  contextDescription?: string;
  exceptionMessage?: string;
  failedCodeSnippets?: FailedCodeSnippet[];
}

export interface WebTauStep {
  children?: WebTauStep[];
  message: TokenizedMessageToken[];
  elapsedTime: number;
  startTime: number;
  personaId?: string;
  input?: WebTauStepInput;
}

export interface TokenizedMessageToken {
  type: string;
  value: any;
}

export interface WebTauStepInput {
  type: string;
  data: any;
}

export type WebTauStepInputKeyValue = { [key: string]: string | number };

export interface StringKeyValue {
  key: string;
  value: string;
}

export interface HttpCall {
  id: string;
  personaId?: string;
  label: string;
  method: string;
  url: string;
  startTime: number;
  elapsedTime: number;
  requestType: string;
  requestBody: any;
  requestHeader: StringKeyValue[];
  responseHeader: StringKeyValue[];
  responseType: string;
  responseBody: any;
  responseStatusCode: number;
  errorMessage?: string;
  mismatches: string[];
  warnings?: string[];
  responseBodyChecks: {
    failedPaths: string[];
    passedPaths: string[];
  };
  test?: WebTauTest;
}

export interface FailedCodeSnippet {
  filePath: string;
  lineNumbers: number[];
  snippet: string;
}
