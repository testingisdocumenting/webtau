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

export interface WebTauTest {
  id: string;
  containerId: string;
  scenario: string;
  steps: WebTauStep[];
  httpCalls?: HttpCall[];
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

export type WebTauStepInputKeyValue = { [key: string]: string };

export interface HttpCall {
  warnings?: string[];
}

export interface FailedCodeSnippet {
  filePath: string;
  lineNumbers: number[];
  snippet: string;
}
