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

import { parseCode } from './codeParser';
import { splitTokensIntoLines } from './codeUtils';

import { LineOfTokens } from './LineOfTokens';

import './SourceCode.css';
import './tokens.css';

interface Props {
  snippet: string;
  filePath?: string;
  lang?: string;
  lineNumbers?: number[];
}

export function SourceCode({ filePath, lang, lineNumbers, snippet }: Props) {
  const tokens = parseCode(lang || 'groovy', snippet);
  const lines = splitTokensIntoLines(tokens);

  return (
    <div className="webtau-source-code">
      {filePath && <div className="file-path">{filePath}</div>}
      <pre>
        {lines.map((line, idx) => (
          <LineOfTokens tokens={line} isHighlighted={lineNumbers && lineNumbers.indexOf(idx + 1) !== -1} />
        ))}
      </pre>
    </div>
  );
}
