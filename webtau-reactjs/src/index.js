/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

import React from 'react'
import ReactDOM from 'react-dom'

import WebTauReport from './report/WebTauReport'
import Report from './report/Report'

import './index.css'

if (process.env.NODE_ENV !== "production") {
    global.testReport = require('./test-data/testData').report
}

global.WebTauReport = WebTauReport

global.renderReport = () => {
    ReactDOM.render(<WebTauReport report={new Report(global.testReport)} />, document.getElementById('root'));
}

global.renderReport()
