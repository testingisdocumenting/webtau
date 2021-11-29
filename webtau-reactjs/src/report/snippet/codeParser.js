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

import * as Prism from 'prismjs';

import 'prismjs/components/prism-java';
import 'prismjs/components/prism-groovy';
import 'prismjs/components/prism-scala';
import 'prismjs/components/prism-json';

export function parseCode(lang, code) {
  const grammar = createGrammar();
  const tokens = tokenize();

  return tokens.map((t) => normalizeToken(t));

  function tokenize() {
    return Prism.tokenize(code, grammar);
  }

  function createGrammar() {
    return Prism.languages[adjustLang(lang)] || Prism.languages.clike;
  }
}

const extensionsMapping = {
  sc: 'scala',
};

function adjustLang(lang) {
  lang = lang ? lang.toLowerCase() : '';
  const extensionBasedLang = extensionsMapping[lang];
  return extensionBasedLang ? extensionBasedLang : lang;
}

function normalizeToken(token) {
  if (typeof token === 'string') {
    return token;
  }

  if (Array.isArray(token.content)) {
    return { type: token.type, content: token.content.map((t) => normalizeToken(t)) };
  }

  return { type: token.type, content: token.content };
}
