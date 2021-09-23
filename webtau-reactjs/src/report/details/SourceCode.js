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

import Prism from 'prismjs';

import 'prismjs/components/prism-groovy';
import 'prismjs/plugins/line-highlight/prism-line-highlight';

import 'prismjs/plugins/line-highlight/prism-line-highlight.css';
import 'prismjs/themes/prism.css';
import 'prismjs/themes/prism-dark.css';

import './SourceCode.css';

class SourceCode extends React.Component {
  render() {
    const { filePath, lineNumbers, snippet } = this.props;

    return (
      <div className="webtau-source-code">
        <div className="file-path">{filePath}</div>
        <pre data-line={lineNumbers.join(',')} className="language-groovy">
          <code>{snippet}</code>
        </pre>
      </div>
    );
  }

  componentDidMount() {
    Prism.highlightAll();
  }

  componentDidUpdate() {
    Prism.highlightAll();
  }
}

export default SourceCode;
