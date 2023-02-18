/*
 * Copyright 2022 webtau maintainers
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

import { Registry } from 'react-component-viewer';
import TestHttpCalls from './TestHttpCalls';

export function httpCallsDemo(registry: Registry) {
  const commonProps = {
    urlState: { httpCallIdxs: '0' },
    onInternalStateUpdate: (update: any) => console.log(update),
    reportNavigation: {},
  };

  registry.add('bad json response', () => (
    <TestHttpCalls {...commonProps} test={{ httpCalls: incompleteJsonHttpCalls() }} />
  ));
}

function incompleteJsonHttpCalls() {
  return [
    {
      id: 'httpCall-1',
      method: 'PUT',
      url: 'http://localhost:54605/invalid-json-response',
      operationId: '',
      startTime: 1655047796968,
      elapsedTime: 90,
      errorMessage: null,
      mismatches: [
        [
          {
            type: 'id',
            value: 'body.temperature',
          },
          {
            type: 'delimiter',
            value: ':',
          },
          {
            type: 'delimiterNoAutoSpacing',
            value: '  ',
          },
          {
            type: 'classifier',
            value: 'actual',
          },
          {
            type: 'delimiter',
            value: ':',
          },
          {
            type: 'styledText',
            value: [
              [
                {
                  styles: [],
                  text: '',
                },
                {
                  styles: ['blue'],
                  text: '88',
                },
              ],
            ],
          },
          {
            type: 'delimiterNoAutoSpacing',
            value: ' ',
          },
          {
            type: 'objectType',
            value: '<java.lang.Integer>',
          },
          {
            type: 'delimiterNoAutoSpacing',
            value: '\n',
          },
          {
            type: 'delimiterNoAutoSpacing',
            value: '                 ',
          },
          {
            type: 'classifier',
            value: 'expected',
          },
          {
            type: 'delimiter',
            value: ':',
          },
          {
            type: 'classifier',
            value: 'less than',
          },
          {
            type: 'styledText',
            value: [
              [
                {
                  styles: [],
                  text: '',
                },
                {
                  styles: ['blue'],
                  text: '10',
                },
              ],
            ],
          },
          {
            type: 'delimiterNoAutoSpacing',
            value: ' ',
          },
          {
            type: 'objectType',
            value: '<java.lang.Integer>',
          },
        ],
      ],
      warnings: [],
      requestHeader: [],
      requestType: 'application/json',
      requestBody: '{"message":"hello"}',
      responseType: 'application/json',
      responseStatusCode: 200,
      responseHeader: [
        {
          key: null,
          value: 'HTTP/1.1 200 OK',
        },
        {
          key: 'Server',
          value: 'Jetty(9.4.44.v20210927)',
        },
        {
          key: 'Content-Type',
          value: 'application/json',
        },
      ],
      responseBody: '{"key": "value',
      responseBodyChecks: { failedPaths: [], passedPaths: [] },
    },
  ];
}
