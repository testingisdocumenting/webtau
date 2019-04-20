package scenarios

import static com.twosigma.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

scenario('summary view') {
    report.open('webtau-report-crud-separated.html')
}

scenario('customer update') {
    report.selectTest('customer update')
    browser.doc.capture('report-test-summary')
}