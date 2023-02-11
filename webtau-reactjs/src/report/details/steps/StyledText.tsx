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

import React from 'react';

import './StyledText.css';

interface StyledPart {
  styles: string[];
  text: string;
}

type StyledLine = StyledPart[];

interface Props {
  lines: StyledLine[];
}

export function StyledText({ lines }: Props) {
  const renderedLines = lines.map(renderLine);
  return <span className="webtau-styled-text">{renderedLines}</span>;

  function renderLine(line: StyledLine, idx: number) {
    const renderedParts = line.map((part, idx) => {
      const className = generateClassNameFromStyles(part.styles);
      return (
        <span key={idx} className={className}>
          {part.text}
        </span>
      );
    });

    return (
      <div className="webtau-styled-line" key={idx}>
        {renderedParts}
      </div>
    );
  }
}

function generateClassNameFromStyles(styles: string[]) {
  return styles.map((style) => 'webtau-styled-' + style).join(' ');
}
