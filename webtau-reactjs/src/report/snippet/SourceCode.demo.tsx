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
    <div className="webtau-dark">
      <SourceCode filePath="scenarios/my-test.groovy" lineNumbers={[2]} snippet={sampleSnippet()} />
    </div>
  ));

  registry.add('light theme', () => (
    <div className="webtau-light">
      <SourceCode filePath="scenarios/my-test.groovy" lineNumbers={[2]} snippet={sampleSnippet()} />
    </div>
  ));

  registry.add('json dark', () => (
    <div className="webtau-dark">
      <SourceCode snippet={sampleJsonSnippet()} lang="json" />
    </div>
  ));

  registry.add('json light', () => (
    <div className="webtau-light">
      <SourceCode snippet={sampleJsonSnippet()} lang="json" />
    </div>
  ));

  registry.add('multi line comment', () => (
    <div className="webtau-light">
      <SourceCode snippet={multiLineComment()} lang="java" lineNumbers={[21, 23]} />
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

function multiLineComment() {
  return `/*
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

package scenarios.concept.handlers

import org.testingisdocumenting.webtau.TestListener

class FailingTestListener implements TestListener {
    @Override
    void beforeFirstTest() {
        throw new RuntimeException("failure")
    }
}
 `;
}
