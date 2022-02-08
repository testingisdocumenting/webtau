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

import { splitTokensIntoLines } from './codeUtils';

import { parseCode } from './codeParser';

const tokens = [
  { type: 'keyword', content: 'class' },
  ' ',
  {
    type: 'class-name',
    content: ['Test'],
  },
  ' Test2 ',
  { type: 'punctuation', content: '{' },
  '\n',
  { type: 'comment', content: '/*another comment line end of comment */' },
  '\n    ',
  { type: 'keyword', content: 'var' },
  ' a  ',
  { type: 'operator', content: '=' },
  ' ',
  {
    type: 'number',
    content: '2',
  },
  { type: 'punctuation', content: ';' },
  ' ',
  { type: 'comment', content: '// comment line' },
  '\n    ',
  { type: 'keyword', content: 'var' },
  ' b ',
  { type: 'operator', content: '=' },
  ' a ',
  {
    type: 'operator',
    content: '+',
  },
  ' ',
  { type: 'number', content: '1' },
  { type: 'punctuation', content: ';' },
  '       ',
  {
    type: 'comment',
    content: '//          another comment',
  },
  '\n    ',
  { type: 'keyword', content: 'var' },
  ' c ',
  { type: 'operator', content: '=' },
  ' ',
  {
    type: 'number',
    content: '3',
  },
  { type: 'punctuation', content: ';' },
  '         ',
  {
    type: 'comment',
    content: '//             in two lines',
  },
  '\n    ',
  { type: 'keyword', content: 'var' },
  ' d ',
  { type: 'operator', content: '=' },
  ' a ',
  {
    type: 'operator',
    content: '+',
  },
  ' ',
  { type: 'number', content: '1' },
  { type: 'punctuation', content: ';' },
  '\n',
];

const codeWithEmptyLines = `public class DocScaffolding {
    private final Path workingDir;

    private Map<String, List<String>> fileNameByDirName;`;

describe('codeUtils', () => {
  describe('split into lines', () => {
    it('split list of tokens into lists of tokens per line', () => {
      const lines = splitTokensIntoLines(tokens);
      expect(lines.length).toEqual(6);

      expect(lines[0]).toEqual([
        { content: 'class', type: 'keyword' },
        ' ',
        { content: ['Test'], type: 'class-name' },
        ' ',
        'Test2 ',
        { content: '{', type: 'punctuation' },
      ]);

      expect(lines[2]).toEqual([
        '    ',
        { content: 'var', type: 'keyword' },
        ' ',
        'a  ',
        { content: '=', type: 'operator' },
        ' ',
        { content: '2', type: 'number' },
        { content: ';', type: 'punctuation' },
        ' ',
        { content: '// comment line', type: 'comment' },
      ]);
    });

    it('creates separate empty lines', () => {
      const tokens = parseCode('java', codeWithEmptyLines);
      const lines = splitTokensIntoLines(tokens);
      expect(lines.length).toEqual(4);
      expect(lines[2]).toEqual([]);
    });

    it('handles string token with new-line code in the middle', () => {
      const tokens = ['hello\n  world'];
      const lines = splitTokensIntoLines(tokens);

      expect(lines.length).toEqual(2);
      expect(lines[0]).toEqual(['hello']);
      expect(lines[1]).toEqual(['  ', 'world']);
    });

    it('converts text token with spacing into separate spacing token and text token', () => {
      const tokens = parseCode('java', ' http.get\n' + '    body.get("price") {\n' + '    body.get("id") {\n');
      const lines = splitTokensIntoLines(tokens);

      expect(lines).toEqual([
        [' ', 'http', { type: 'punctuation', content: '.' }, 'get'],
        [
          '    ',
          'body',
          { type: 'punctuation', content: '.' },
          { type: 'function', content: 'get' },
          { type: 'punctuation', content: '(' },
          { type: 'string', content: '"price"' },
          { type: 'punctuation', content: ')' },
          ' ',
          { type: 'punctuation', content: '{' },
        ],
        [
          '    ',
          'body',
          { type: 'punctuation', content: '.' },
          { type: 'function', content: 'get' },
          { type: 'punctuation', content: '(' },
          { type: 'string', content: '"id"' },
          { type: 'punctuation', content: ')' },
          ' ',
          { type: 'punctuation', content: '{' },
        ],
      ]);
    });

    it('splits multi line comment into separate lines', () => {
      const tokens = parseCode(
        'java',
        `  /** hello
  multi line
  comment
  */

class MyClass {
}
`
      );
      const lines = splitTokensIntoLines(tokens);

      expect(lines).toEqual([
        ['  ', { type: 'comment', content: '/** hello' }],
        [{ type: 'comment', content: '  multi line' }],
        [{ type: 'comment', content: '  comment' }],
        [{ type: 'comment', content: '  */' }],
        [],
        [
          { type: 'keyword', content: 'class' },
          ' ',
          { type: 'class-name', content: 'MyClass' },
          ' ',
          { type: 'punctuation', content: '{' },
        ],
        [{ type: 'punctuation', content: '}' }],
      ]);
    });
  });
});
