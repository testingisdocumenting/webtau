/*
 * Copyright 2020 webtau maintainers
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

import TestSteps from './details/TestSteps';
import TestHttpCalls from './details/http/TestHttpCalls';
import ShortStackTrace from './details/ShortStackTrace';
import Screenshot from './details/Screenshot';
import FullStackTrace from './details/FullStackTrace';
import { TestSummary } from './details/TestSummary';
import StatusEnum from './StatusEnum';
import PerformanceReport from './PerformanceReport';
import TestCliCalls from './details/cli/TestCliCalls';
import TestCliBackground from './details/cli/TestCliBackground';
import { TestServerJournals } from './details/servers/TestServerJournals';

class Report {
  static overallHttpCallTimeForTest(test) {
    const httpCalls = test.httpCalls || [];
    const times = httpCalls.map((c) => c.elapsedTime);
    return times.reduce((a, r) => a + r, 0);
  }

  static averageHttpCallTimeForTest(test) {
    if (!test.httpCalls || test.httpCalls.length === 0) {
      return 0;
    }

    const overallTime = Report.overallHttpCallTimeForTest(test);
    return overallTime / test.httpCalls.length;
  }

  static groupTestsByContainerWithFailedAtTheTop(tests) {
    return groupTestsByContainerWithFailedAtTheTop(tests);
  }

  static groupTestsByContainer(tests) {
    return groupTestsByContainer(tests);
  }

  constructor(report) {
    this.report = report;
    this.name = report.name;
    this.nameUrl = report.nameUrl;
    this.version = report.version;
    this.config = report.config;
    this.envVars = report.envVars;
    this.summary = expandSummary(report.summary);
    this.tests = enrichTestsData(report.tests);
    this.httpCalls = extractHttpCalls(this.tests);
    this.httpCallsById = mapHttpCallsById(this.httpCalls);
    this.openApiHttpCallIdsPerOperation = report.openApiHttpCallIdsPerOperation || [];
    this.performance = new PerformanceReport(this);
    this.httpPerformance = report.httpPerformance;
    this.httpCallsCombinedWithSkipped = [
      ...convertSkippedToHttpCalls(report.openApiSkippedOperations || []),
      ...this.httpCalls,
    ];
    this.testsSummary = buildTestsSummary(report.summary);
    this.httpCallsSummary = buildHttpCallsSummary(this.httpCallsCombinedWithSkipped);
  }

  findTestById(id) {
    const found = this.tests.filter((t) => t.id === id);
    return found.length ? found[0] : null;
  }

  findHttpCallById(id) {
    const found = this.httpCallsCombinedWithSkipped.filter((t) => t.id === id);
    return found.length ? found[0] : null;
  }

  hasHttpOperationCoverage() {
    return !!this.report.openApiSkippedOperations;
  }

  openApiOperationsCoverage() {
    const openApiCoveredOperations = this.numberOfOpenApiCoveredOperations();
    const openApiSkippedOperations = this.numberOfOpenApiSkippedOperations();

    const total = openApiCoveredOperations + openApiSkippedOperations;

    if (total === 0) {
      return 0;
    }

    return openApiCoveredOperations / total;
  }

  numberOfOpenApiCoveredOperations() {
    return (this.report.openApiCoveredOperations || []).length;
  }

  numberOfOpenApiSkippedOperations() {
    return (this.report.openApiSkippedOperations || []).length;
  }

  numberOfHttpCalls() {
    return this.httpCalls.length;
  }

  hasHttpCalls() {
    return this.numberOfHttpCalls() > 0;
  }

  overallHttpCallTime() {
    return this.httpCalls.map((c) => c.elapsedTime).reduce((prev, curr) => prev + curr, 0);
  }

  averageHttpCallTime() {
    const n = this.numberOfHttpCalls();
    if (!n) {
      return 0;
    }

    return this.overallHttpCallTime() / n;
  }

  testsWithStatusAndFilteredByText(status, text) {
    return this.tests.filter(
      (t) =>
        statusFilterPredicate(t.status, status) &&
        (textFilterPredicate(t.scenario, text) ||
          textFilterPredicate(t.shortContainerId, text) ||
          textFilterPredicate(t.containerId, text))
    );
  }

  httpCallsWithStatusAndFilteredByText(status, text) {
    return this.httpCallsCombinedWithSkipped.filter(
      (c) =>
        statusFilterPredicate(c.status, status) &&
        (textFilterPredicate(c.shortUrl, text) || textStartOnlyFilterPredicate(c.method, text))
    );
  }

  hasTestWithId(id) {
    return this.findTestById(id) !== null;
  }

  hasDetailWithTabName(testId, tabName) {
    const test = this.findTestById(testId);
    if (!test) {
      return false;
    }

    return test.details.filter((d) => d.tabName === tabName).length !== 0;
  }

  firstDetailTabName(testId) {
    const test = this.findTestById(testId);
    if (!test) {
      return '';
    }

    return test.details[0].tabName;
  }
}

function expandSummary(summary) {
  return {
    ...summary,
    totalRan: calculateTotalRan(),
    totalWithProblems: calculateTotalWithProblems(),
    percentagePassed: calculatePercentagePassed(),
    percentageRan: calculatePercentageRan(),
  };

  function calculateTotalWithProblems() {
    return summary.failed + summary.errored;
  }

  function calculateTotalRan() {
    return summary.total - summary.skipped;
  }

  function calculatePercentagePassed() {
    return (100 - (calculateTotalWithProblems() / calculateTotalRan()) * 100) | 0;
  }

  function calculatePercentageRan() {
    return ((calculateTotalRan() / summary.total) * 100) | 0;
  }
}

function extractHttpCalls(tests) {
  return tests
    .filter((t) => t.httpCalls)
    .map((t) => enrichHttpCallsData(t, t.httpCalls))
    .reduce((acc, r) => acc.concat(r), []);
}

function mapHttpCallsById(httpCalls) {
  const result = {};
  httpCalls.forEach((httpCall) => {
    result[httpCall.id] = httpCall;
  });

  return result;
}

function convertSkippedToHttpCalls(skippedCalls) {
  return skippedCalls.map((c) => enrichSkippedHttpCall(c));
}

function statusFilterPredicate(actualStatus, status) {
  if (!status || status === 'Total') {
    return true;
  }

  return actualStatus === status;
}

function textFilterPredicate(actualText, text) {
  return lowerCaseIndexOf(actualText, text) !== -1;
}

function textStartOnlyFilterPredicate(actualText, text) {
  return lowerCaseIndexOf(actualText, text) === 0;
}

function lowerCaseIndexOf(text, part) {
  if (!text) {
    return -1;
  }

  return text.toLowerCase().indexOf(part.toLowerCase());
}

function enrichTestsData(tests) {
  return tests.map((test) => ({
    ...test,
    containerId: shortContainerId(test),
    shortContainerId: shortContainerId(test),
    details: additionalDetails(test),
    httpCalls: enrichHttpCallsData(test, test.httpCalls),
  }));
}

function groupTestsByContainerWithFailedAtTheTop(tests) {
  const groups = groupTestsByContainer(tests);
  return groupWithFailedTestsAtTheTop(groups);
}

function groupTestsByContainer(tests) {
  const groups = [];
  const groupById = {};

  tests.forEach((t) => {
    const groupId = t.containerId;

    let group = groupById[groupId];
    if (!group) {
      group = { id: groupId, tests: [] };
      groupById[groupId] = group;
      groups.push(group);
    }

    group.tests.push(t);
  });

  return groups;
}

function shortContainerId(test) {
  if (test.shortContainerId) {
    return test.shortContainerId;
  } else {
    return shortenClassName(test.className);
  }
}

function shortenClassName(className) {
  const lastDotIdx = className ? className.lastIndexOf('.') : -1;
  return lastDotIdx === -1 ? className : className.substr(lastDotIdx + 1);
}

function groupWithFailedTestsAtTheTop(groups) {
  const failed = groups.filter((g) => hasFailedOrErrored(g));
  const rest = groups.filter((g) => !hasFailedOrErrored(g));

  return [...failed, ...rest];
}

function hasFailedOrErrored(group) {
  return group.tests.some((t) => isFailedOrErrored(t));
}

function isFailedOrErrored(test) {
  return test.status === StatusEnum.FAILED || test.status === StatusEnum.ERRORED;
}

function enrichHttpCallsData(test, httpCalls = []) {
  return httpCalls.map((httpCall) => enrichHttpCallData(test, httpCall));
}

function buildTestsSummary(summary) {
  return [
    { label: 'Total', value: summary.total },
    { label: StatusEnum.PASSED, value: summary.passed },
    { label: StatusEnum.FAILED, value: summary.failed },
    { label: StatusEnum.SKIPPED, value: summary.skipped },
    { label: StatusEnum.ERRORED, value: summary.errored },
  ];
}

function buildHttpCallsSummary(httpCallsCombinedWithSkipped) {
  return [
    { label: 'Total', value: httpCallsCombinedWithSkipped.length },
    { label: StatusEnum.SKIPPED, value: count(StatusEnum.SKIPPED) },
    { label: StatusEnum.PASSED, value: count(StatusEnum.PASSED) },
    { label: StatusEnum.FAILED, value: count(StatusEnum.FAILED) },
    { label: StatusEnum.ERRORED, value: count(StatusEnum.ERRORED) },
  ];

  function count(status) {
    return httpCallsCombinedWithSkipped.filter((c) => c.status === status).length;
  }
}

function deriveHttpCallStatus(httpCall) {
  if (httpCall.mismatches.length > 0) {
    return StatusEnum.FAILED;
  }

  if (httpCall.errorMessage) {
    return StatusEnum.ERRORED;
  }

  return StatusEnum.PASSED;
}

function enrichHttpCallData(test, httpCall) {
  const shortUrl = removeHostFromUrl(httpCall.url);

  return {
    ...httpCall,
    shortUrl,
    test,
    status: deriveHttpCallStatus(httpCall),
    label: httpCall.method + ' ' + shortUrl,
  };
}

function enrichSkippedHttpCall(skipped) {
  return {
    id: generateHttpCallId(),
    shortUrl: skipped.url,
    method: skipped.method,
    status: StatusEnum.SKIPPED,
    label: skipped.method + ' ' + skipped.url,
  };
}

function removeHostFromUrl(url) {
  const doubleSlashPattern = '://';
  const doubleSlashStartIdx = url.indexOf(doubleSlashPattern);

  if (doubleSlashStartIdx === -1) {
    return url;
  }

  const firstUrlSlashIdx = url.indexOf('/', doubleSlashStartIdx + doubleSlashPattern.length);
  return url.substr(firstUrlSlashIdx);
}

let lastId = 1;
function generateHttpCallId() {
  return 'httpcall-skipped-' + lastId++;
}

function additionalDetails(test) {
  const details = [];
  details.push({ tabName: 'Summary', component: TestSummary });

  if (test.hasOwnProperty('screenshot')) {
    details.push({ tabName: 'Screenshot', component: Screenshot });
  }

  if (test.hasOwnProperty('httpCalls') && test.httpCalls.length > 0) {
    details.push({ tabName: 'HTTP calls', component: TestHttpCalls });
  }

  if (test.hasOwnProperty('servers') && test.servers.length > 0) {
    details.push({ tabName: 'Servers', component: TestServerJournals });
  }

  if (test.hasOwnProperty('cliCalls') && test.cliCalls.length > 0) {
    details.push({ tabName: 'CLI calls', component: TestCliCalls });
  }

  if (test.hasOwnProperty('cliBackground') && test.cliBackground.length > 0) {
    details.push({ tabName: 'CLI in background', component: TestCliBackground });
  }

  if (test.hasOwnProperty('steps')) {
    details.push({ tabName: 'Steps', component: TestSteps });
  }

  if (test.hasOwnProperty('shortStackTrace')) {
    details.push({ tabName: 'StackTrace', component: ShortStackTrace });
  }

  if (test.hasOwnProperty('fullStackTrace')) {
    details.push({ tabName: 'Full StackTrace', component: FullStackTrace });
  }

  return details;
}

export default Report;
