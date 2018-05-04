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
import { storiesOf } from '@storybook/react'

import StepsDemo from './StepsDemo'
import WebTauReportDemo from './WebTauReportDemo'
import {TestDetailsWithScreenshot, TestDetailsWithStackTrace, TestDetailsWithHttpCall} from "./TestDetailsDemo";

import './WebTauReport.css'

storiesOf('Report', module)
    .add('default view', () => <WebTauReportDemo/>)

storiesOf('TestDetails', module)
    .add('with screenshot', () => <TestDetailsWithScreenshot/>)
    .add('with stack trace', () => <TestDetailsWithStackTrace/>)
    .add('with http calls', () => <TestDetailsWithHttpCall/>)

storiesOf('Steps', module)
    .add('with errors', () => <StepsDemo/>)
