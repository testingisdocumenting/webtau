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
import { Step } from './Step';
import { Registry } from 'react-component-viewer';
import { WebTauStep } from '../../WebTauTest';
import { wrapInDarkTheme, wrapInLightTheme } from '../../demoUtils';

export function stepsDemo(registry: Registry) {
  add('no children', <Step step={noChildren()} isTopLevel={true} />);
  add('zero elapsed time', <Step step={zeroElapsedTime()} isTopLevel={true} />);
  add('with children', <Step step={withChildren()} isTopLevel={true} />);
  add('with failed children', <Step step={withFailedChildren()} isTopLevel={true} />);
  add('rainbow', <Step step={rainbow()} isTopLevel={true} />);
  add('with styled text input', <Step step={withStyledTextInput()} isTopLevel={true} />);
  add('with key value input', <Step step={withKeyValueInput()} isTopLevel={true} />);
  add('with key value output', <Step step={withKeyValueOutput()} isTopLevel={true} />);
  add('with key value input output map', <Step step={withKeyValueInputAndOutputMap()} isTopLevel={true} />);
  add('with key value empty input output', <Step step={withKeyValueEmptyInputAndOutput()} isTopLevel={true} />);
  add('with actual value output', <Step step={withActualValueStepOutput()} isTopLevel={true} />);

  function add(label: string, element: JSX.Element) {
    registry.add(label + ' [dark]', wrapInDarkTheme(element));
    registry.add(label + ' [light]', wrapInLightTheme(element));
  }
}

function noChildren() {
  return {
    elapsedTime: 200,
    startTime: 0,
    isSuccessful: true,
    message: [
      {
        type: 'action',
        value: 'executed HTTP GET',
      },
      {
        type: 'url',
        value: 'http://localhost:8080/customers/1',
      },
    ],
  };
}

function zeroElapsedTime() {
  return {
    elapsedTime: 0,
    startTime: 0,
    isSuccessful: true,
    message: [
      {
        type: 'action',
        value: 'executed HTTP GET',
      },
      {
        type: 'url',
        value: 'http://localhost:8080/customers/1',
      },
    ],
  };
}

function rainbow() {
  return {
    elapsedTime: 200,
    startTime: 0,
    isSuccessful: true,
    message: [
      {
        type: 'action',
        value: 'executed HTTP GET',
      },
      {
        type: 'warning',
        value: 'something is fishy',
      },
      {
        type: 'url',
        value: 'http://localhost:8080/customers/1',
      },
      {
        type: 'preposition',
        value: ' of',
      },
      {
        type: 'id',
        value: 'identifier',
      },
      {
        type: 'stringValue',
        value: '"hello"',
      },
      {
        type: 'delimiter',
        value: '--',
      },
      {
        type: 'error',
        value: 'ops',
      },
      {
        type: 'selectorType',
        value: 'by css',
      },
      {
        type: 'selectorValue',
        value: '.classn',
      },
    ],
  };
}

function withChildren(): WebTauStep {
  return {
    ...noChildren(),
    startTime: 0,
    children: [
      {
        startTime: 0,
        elapsedTime: 50,
        isSuccessful: true,
        message: [
          {
            type: 'id',
            value: 'body',
          },
          {
            type: 'matcher',
            value:
              'equals {firstName=FN, lastName=LN}\nmatches:\n\nbody.firstName:   actual: "FN" <java.lang.String>\n                expected: "FN" <java.lang.String>\nbody.lastName:   actual: "LN" <java.lang.String>\n               expected: "LN" <java.lang.String>',
          },
        ],
      },
      {
        elapsedTime: 150,
        startTime: 0,
        isSuccessful: true,
        message: [
          {
            type: 'id',
            value: 'header.statusCode',
          },
          {
            type: 'matcher',
            value:
              'equals 200\nmatches:\n\nheader.statusCode:   actual: 200 <java.lang.Integer>\n                   expected: 200 <java.lang.Integer>',
          },
        ],
      },
    ],
  };
}

function withFailedChildren(): WebTauStep {
  return {
    isSuccessful: false,
    message: [
      {
        type: 'error',
        value: 'failed',
      },
      {
        type: 'action',
        value: 'executing HTTP GET',
      },
      {
        type: 'url',
        value: 'http://localhost:57137/weather',
      },
    ],
    exceptionTokenizedMessage: [
      {
        type: 'error',
        value: 'see the failed assertion details above',
      },
    ],
    startTime: 1675530264809,
    elapsedTime: 166,
    children: [
      {
        message: [
          {
            type: 'error',
            value: 'failed',
          },
          {
            type: 'action',
            value: 'expecting',
          },
          {
            type: 'id',
            value: 'body.temperature',
          },
          {
            type: 'matcher',
            value: 'to be less than 10',
          },
        ],
        exceptionTokenizedMessage: [
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
        isSuccessful: false,
        startTime: 1675530264927,
        elapsedTime: 28,
        classifier: 'matcher',
      },
    ],
    input: {
      type: 'HttpStepInput',
      data: {},
    },
    output: {
      type: 'HttpValidationResult',
      data: {
        id: 'httpCall-1',
        method: 'GET',
        url: 'http://localhost:57137/weather',
        operationId: '',
        startTime: 1675530264811,
        elapsedTime: 73,
        errorMessage: null,
        mismatches: [
          'mismatches:\n\nbody.temperature:   actual: 88 <java.lang.Integer>\n                  expected: less than 10 <java.lang.Integer>',
        ],
        requestHeader: [],
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
            key: 'Vary',
            value: 'Accept-Encoding, User-Agent',
          },
          {
            key: 'Content-Length',
            value: '18',
          },
          {
            key: 'Date',
            value: 'Sat, 04 Feb 2023 17:04:24 GMT',
          },
          {
            key: 'Content-Type',
            value: 'application/json',
          },
        ],
        responseBody: '{"temperature":88}',
        responseBodyChecks: {
          failedPaths: ['root.temperature'],
          passedPaths: [],
        },
      },
    },
  };
}

function withKeyValueInputAndOutputMap() {
  return {
    isSuccessful: true,
    message: [
      { type: 'action', value: 'set' },
      { type: 'id', value: 'url' },
    ],
    startTime: 1621811973852,
    elapsedTime: 0,
    input: { type: 'WebTauStepInputKeyValue', data: { source: 'manual', url: 'http://localhost:64934', cost: 150 } },
    output: { type: 'WebTauStepOutputKeyValue', data: { port: 3473 } },
  };
}

function withActualValueStepOutput() {
  return {
    message: [
      {
        type: 'error',
        value: 'failed',
      },
      {
        type: 'action',
        value: 'expecting',
      },
      {
        type: 'id',
        value: '[value]',
      },
      {
        type: 'matcher',
        value: 'to equal',
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
              styles: ['yellow'],
              text: '[',
            },
          ],
          [
            {
              styles: [],
              text: '  ',
            },
            {
              styles: ['cyan'],
              text: '1',
            },
            {
              styles: ['yellow'],
              text: ',',
            },
          ],
          [
            {
              styles: [],
              text: '  ',
            },
            {
              styles: ['cyan'],
              text: '4',
            },
            {
              styles: ['yellow'],
              text: ',',
            },
          ],
          [
            {
              styles: [],
              text: '  ',
            },
            {
              styles: ['cyan'],
              text: '3',
            },
          ],
          [
            {
              styles: [],
              text: '',
            },
            {
              styles: ['yellow'],
              text: ']',
            },
          ],
        ],
      },
      {
        type: 'delimiter',
        value: ':',
      },
      {
        type: 'error',
        value:
          '\nmismatches:\n\n[value][1]:   actual: 2 <java.lang.Integer>\n            expected: 4 <java.lang.Integer>',
      },
    ],
    startTime: 1676323554991,
    elapsedTime: 42,
    isSuccessful: false,
    classifier: 'matcher',
    output: {
      type: 'ValueMatcherStepOutput',
      data: {
        styledText: [
          [
            {
              styles: [],
              text: '',
            },
          ],
          [
            {
              styles: [],
              text: '',
            },
            {
              styles: ['yellow'],
              text: '[',
            },
          ],
          [
            {
              styles: [],
              text: '  ',
            },
            {
              styles: ['cyan'],
              text: '1',
            },
            {
              styles: ['yellow'],
              text: ',',
            },
          ],
          [
            {
              styles: [],
              text: '  ',
            },
            {
              styles: ['red'],
              text: '**2**',
            },
            {
              styles: ['yellow'],
              text: ',',
            },
          ],
          [
            {
              styles: [],
              text: '  ',
            },
            {
              styles: ['cyan'],
              text: '3',
            },
          ],
          [
            {
              styles: [],
              text: '',
            },
            {
              styles: ['yellow'],
              text: ']',
            },
          ],
        ],
      },
    },
  };
}

function withKeyValueEmptyInputAndOutput() {
  return {
    isSuccessful: true,
    message: [
      { type: 'action', value: 'set' },
      { type: 'id', value: 'url' },
    ],
    startTime: 1621811973852,
    elapsedTime: 0,
    input: { type: 'WebTauStepInputKeyValue', data: {} },
    output: { type: 'WebTauStepOutputKeyValue', data: {} },
  };
}

function withKeyValueInput() {
  return {
    isSuccessful: true,
    message: [
      { type: 'action', value: 'set' },
      { type: 'id', value: 'url' },
    ],
    startTime: 1621811973852,
    elapsedTime: 0,
    input: { type: 'WebTauStepInputKeyValue', data: { source: 'manual', url: 'http://localhost:64934', cost: 150 } },
  };
}

function withKeyValueOutput() {
  return {
    isSuccessful: true,
    message: [
      { type: 'action', value: 'set' },
      { type: 'id', value: 'url' },
    ],
    startTime: 1621811973852,
    elapsedTime: 0,
    output: { type: 'WebTauStepOutputKeyValue', data: { port: 3473 } },
  };
}

function withStyledTextInput() {
  return {
    message: [
      {
        type: 'action',
        value: 'accounts',
      },
    ],
    startTime: 1676999954880,
    elapsedTime: 0,
    isSuccessful: true,
    classifier: 'trace',
    input: {
      type: 'WebTauStepInputPrettyPrint',
      data: {
        styledText: [
          [
            {
              styles: [],
              text: '',
            },
            {
              styles: ['purple'],
              text: 'id  ',
            },
            {
              styles: ['yellow'],
              text: ' │ ',
            },
            {
              styles: ['purple'],
              text: 'money           ',
            },
            {
              styles: ['yellow'],
              text: ' │ ',
            },
            {
              styles: ['purple'],
              text: 'name           ',
            },
          ],
          [
            {
              styles: [],
              text: '',
            },
            {
              styles: ['green'],
              text: '"a1"',
            },
            {
              styles: ['yellow'],
              text: ' │ ',
            },
            {
              styles: ['yellow'],
              text: '{',
            },
            {
              styles: ['purple'],
              text: '"dollars"',
            },
            {
              styles: ['yellow'],
              text: ': ',
            },
            {
              styles: ['blue'],
              text: '100',
            },
            {
              styles: ['yellow'],
              text: '}',
            },
            {
              styles: ['yellow'],
              text: ' │ ',
            },
            {
              styles: ['green'],
              text: '"Account One"  ',
            },
          ],
          [
            {
              styles: [],
              text: '',
            },
            {
              styles: ['green'],
              text: '"a2"',
            },
            {
              styles: ['yellow'],
              text: ' │ ',
            },
            {
              styles: ['yellow'],
              text: '{',
            },
            {
              styles: ['purple'],
              text: '"dollars"',
            },
            {
              styles: ['yellow'],
              text: ': ',
            },
            {
              styles: ['blue'],
              text: '130',
            },
            {
              styles: ['yellow'],
              text: '}',
            },
            {
              styles: ['yellow'],
              text: ' │ ',
            },
            {
              styles: ['green'],
              text: '"Account Two"  ',
            },
          ],
          [
            {
              styles: [],
              text: '',
            },
            {
              styles: ['green'],
              text: '"a3"',
            },
            {
              styles: ['yellow'],
              text: ' │ ',
            },
            {
              styles: ['yellow'],
              text: '{',
            },
            {
              styles: ['purple'],
              text: '"dollars"',
            },
            {
              styles: ['yellow'],
              text: ': ',
            },
            {
              styles: ['blue'],
              text: '70',
            },
            {
              styles: ['yellow'],
              text: '} ',
            },
            {
              styles: ['yellow'],
              text: ' │ ',
            },
            {
              styles: ['green'],
              text: '"Account Three"',
            },
          ],
        ],
      },
    },
  };
}
