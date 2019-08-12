package scenarios

import static com.twosigma.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

scenario('summary view') {
    report.open('rest/springboot/customerCrudSeparatedMissingMethod-webtau-report.html')
    browser.doc.capture('report-summary')
}

scenario('customer update test summary') {
    report.selectTest('customer update')
    browser.doc.capture('report-test-summary')
}

scenario('customer update http calls') {
    report.open('rest/springboot/customerCrudSeparated-webtau-report.html')
    report.selectTest('customer update')

    report.selectHttpCalls()
    report.expandHttpCall(2)

    browser.doc.withAnnotations(
            browser.doc.highlight(report.fullScreenIcon),
            browser.doc.highlight(report.collapsedHeader))
            .capture('report-crud-separated-http-calls')
}