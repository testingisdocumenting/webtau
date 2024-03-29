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

import React from "react";
import WebTauReport from "./WebTauReport";
import Report from "./Report";

import { basicReport, withCliDataReport, withRestDataReport } from "../test-data/testData";
import { Registry } from "react-component-viewer";

export function webTauReportsDemo(registry: Registry) {
  registry.add("basic", () => <WebTauReport report={new Report(basicReport)} />);
  registry.add("with REST", () => <WebTauReport report={new Report(withRestDataReport)} />);
  registry.add("with CLI", () => <WebTauReport report={new Report(withCliDataReport)} />);
}
