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

import { Registry } from 'react-component-viewer';
import { SourceCode } from './SourceCode';

export function sourceCodeDemo(registry: Registry) {
  registry.add('dark theme', () => (
    <div>
      <SourceCode filePath="scenarios/my-test.groovy" lineNumbers={[2]} snippet={sampleSnippet()} />
    </div>
  ));

  registry.add('light theme', () => (
    <div className="webtau-light">
      <SourceCode filePath="scenarios/my-test.groovy" lineNumbers={[2]} snippet={sampleSnippet()} />
    </div>
  ));

  registry.add('json dark', () => (
    <div>
      <SourceCode snippet={sampleJsonSnippet()} lang="json" />
    </div>
  ));

  registry.add('json light', () => (
    <div className="webtau-light">
      <SourceCode snippet={sampleJsonSnippet()} lang="json" />
    </div>
  ));
}

function sampleSnippet() {
  return `scenario("test") {
  http.post(...);
}
`;
}

function sampleJsonSnippet() {
  return `{
  "key": "value"
}`;
}
