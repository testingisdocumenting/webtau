/*
 * Copyright 2023 webtau maintainers
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
import { TokenizedMessage } from './TokenizedMessage';
import { wrapInDarkTheme, wrapInLightTheme } from '../../demoUtils';

import './TokenizedMessage.demo.css';

export function tokenizedMessageDemo(registry: Registry) {
  add('styled text', <TokenizedMessage message={messageWithStyledText()} />);
  add('url and styled text', <TokenizedMessage message={messageWithUrlAndStyledText()} />);

  function add(label: string, element: JSX.Element) {
    registry.add(label + ' [dark]', wrapInDarkTheme(<div className="message-demo-background">{element}</div>));
    registry.add(label + ' [light]', wrapInLightTheme(<div className="message-demo-background">{element}</div>));
  }
}

function messageWithUrlAndStyledText() {
  return [
    {
      type: 'id',
      value: 'received',
    },
    {
      type: 'preposition',
      value: 'from',
    },
    {
      type: 'url',
      value: 'ws://localhost:8080/prices',
    },
    {
      type: 'matcher',
      value: 'equals',
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
            text: '{',
          },
          {
            styles: ['purple'],
            text: '"price"',
          },
          {
            styles: ['yellow'],
            text: ': ',
          },
          {
            styles: ['cyan'],
            text: '<greater than 100>',
          },
          {
            styles: ['yellow'],
            text: ', ',
          },
          {
            styles: ['purple'],
            text: '"symbol"',
          },
          {
            styles: ['yellow'],
            text: ': ',
          },
          {
            styles: ['green'],
            text: '"IBM"',
          },
          {
            styles: ['yellow'],
            text: '}',
          },
        ],
      ],
    },
  ];
}

function messageWithStyledText() {
  return [
    {
      type: 'selectorType',
      value: 'by css',
    },
    {
      type: 'selectorValue',
      value: '#split ul li',
    },
    {
      type: 'matcher',
      value: 'equals',
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
            text: '100',
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
            text: '28',
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
            text: '172.6',
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
  ];
}
