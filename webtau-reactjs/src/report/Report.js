import TestSteps from './details/TestSteps'
import HttpCalls from './details/http/HttpCalls'
import ShortStackTrace from './details/ShortStackTrace'
import Screenshot from './details/Screenshot'
import FullStackTrace from './details/FullStackTrace'
import Summary from './details/Summary'

class Report {
    static overallHttpCallTimeForTest(test) {
        const times = test.httpCalls.map(c => c.elapsedTime)
        return times.reduce((a, r) => a + r, 0)
    }

    static averageHttpCallTimeForTest(test) {
        const overallTime = Report.overallHttpCallTimeForTest(test)
        return overallTime / test.httpCalls.length
    }

    constructor(report) {
        this.report = report
        this.summary = report.summary
        this.tests = enrichWithAdditionalDetails(report.tests)
    }

    findTestById(id) {
        const found = this.tests.filter(t => t.id === id)
        return found.length ? found[0] : null
    }

    filterByText(text) {
        return this.tests.filter(t => textFilterPredicate(t, text))
    }

    numberOfHttpCalls() {
        return this.tests
            .map(t => (t.httpCalls || []).length)
            .reduce((prev, curr) => prev + curr, 0)
    }

    overallHttpCallTime() {
        return this.tests
            .map(t => Report.overallHttpCallTimeForTest(t))
            .reduce((prev, curr) => prev + curr, 0)
    }

    averageHttpCallTime() {
        const n = this.numberOfHttpCalls()
        if (!n) {
            return 0
        }

        return this.overallHttpCallTime() / n
    }

    withStatusAndFilteredByText(status, text) {
        if (!status || status === 'Total') {
            return this.filterByText(text)
        }

        return this.tests.filter(t => t.status === status &&
            textFilterPredicate(t, text))
    }

    hasTestWithId(id) {
        return this.findTestById(id) !== null
    }

    hasDetailWithTabName(testId, tabName) {
        const test = this.findTestById(testId)
        if (!test) {
            return false
        }

        return test.details.filter(d => d.tabName === tabName).length !== 0
    }

    firstDetailTabName(testId) {
        const test = this.findTestById(testId)
        if (!test) {
            return ''
        }

        return test.details[0].tabName
    }
}

function textFilterPredicate(test, text) {
    if (!text) {
        return true
    }

    return test.scenario.indexOf(text) !== -1
}

function enrichWithAdditionalDetails(tests) {
    return tests.map(test => ({
            ...test,
            details: additionalDetails(test)
        }))
}

function additionalDetails(test) {
    const details = []
    details.push({tabName: 'Summary', component: Summary})

    if (test.hasOwnProperty('screenshot')) {
        details.push({tabName: 'Screenshot', component: Screenshot})
    }

    if (test.hasOwnProperty('httpCalls')) {
        details.push({tabName: 'HTTP calls', component: HttpCalls})
    }

    if (test.hasOwnProperty('steps')) {
        details.push({tabName: 'Steps', component: TestSteps})
    }

    if (test.hasOwnProperty('shortStackTrace')) {
        details.push({tabName: 'StackTrace', component: ShortStackTrace})
    }

    if (test.hasOwnProperty('fullStackTrace')) {
        details.push({tabName: 'Full StackTrace', component: FullStackTrace})
    }

    return details
}

export default Report