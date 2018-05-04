import React from 'react'
import TestDetails from './TestDetails';

import steps from './testSteps'

import screenshot from './demoScreenshot'

const test = {
    fileName: 'testFile.groovy',
    id: 'testFile.groovy-1',
    status: 'Failed',
    steps: steps,
    scenario: 'User opens an empty order',
    screenshot: screenshot
}

const TestDetailsWithScreenshot = () => <TestDetails test={{
    ...test,
    assertion: 'expected 4\nactual 3\n',
    exceptionMessage: 'AssertionError: expected 4\nactual 3\n',
    contextDescription: 'by css #id'
}}/>

const TestDetailsWithStackTrace = () => <TestDetails test={{
    ...test,
    exceptionMessage: 'division by zero',
    shortStackTrace: 'at class1.groovy\nat class2.groovy',
    fullStackTrace: 'at class1.groovy\nat class2.groovy\nat class3.groovy\nat class4.groovy'
}}/>

const TestDetailsWithHttpCall = () => <TestDetails test={{
    ...test,
    exceptionMessage: 'access weather end point',
    httpCalls: [{
        'method': 'GET',
        'url': 'http://localhost:8180/weather',
        'responseType': 'application/json',
        'responseBody': '{"temperature": 20}\n',
        'responseBodyChecks': {
            'failedPaths': [],
            'passedPaths': [
                'root.temperature'
            ]
        }
    }, {
        method: 'GET',
        url: 'http://weather/station/new-york',
        responseType: 'application/json',
        responseBody: '{"key1": "value1", "key2": "value2", "key3": {"key31": "value31"}}',
        responseBodyChecks: {
            failedPaths: ['root.key3.key31'],
            passedPaths: ['root.key1']
        }
    }]
}}/>

export {
    TestDetailsWithScreenshot,
    TestDetailsWithStackTrace,
    TestDetailsWithHttpCall
}
