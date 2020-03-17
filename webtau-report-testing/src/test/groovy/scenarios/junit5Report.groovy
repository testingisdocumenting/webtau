package scenarios

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

scenario('test lists') {
    report.openJunit5ExampleReport()

    report.groupNames.should contain('customer query')
    report.selectTest('update customer')
}