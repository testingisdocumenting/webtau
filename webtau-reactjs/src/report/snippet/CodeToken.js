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

import { isSimpleValueToken } from './codeUtils';

import 'prismjs/themes/prism-coy.css';

const CodeToken = ({ token }) => {
  if (isSimpleValueToken(token)) {
    return <React.Fragment>{token}</React.Fragment>;
  }

  const className = token.type === 'text' ? '' : 'token ' + token.type;
  return renderSpan(token, className);
};

function renderSpan(token, className) {
  return (
    <span className={className} onClick={token.onClick}>
      {renderData(token)}
    </span>
  );
}

function renderData(token) {
  if (isSimpleValueToken(token)) {
    return token;
  }

  if (Array.isArray(token.content)) {
    return token.content.map((d, idx) => <CodeToken key={idx} token={d} />);
  }

  if (typeof token === 'object') {
    return <CodeToken token={token.content} onClick={token.onClick} />;
  }

  return JSON.stringify(token);
}

export default CodeToken;
