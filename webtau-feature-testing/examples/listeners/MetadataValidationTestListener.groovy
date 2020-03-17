package listeners

import org.testingisdocumenting.webtau.reporter.TestListener
import org.testingisdocumenting.webtau.reporter.WebTauTest

class MetadataValidationTestListener implements TestListener {
    @Override
    void afterLastTestStatement(WebTauTest test) {
        if (!test.metadata.has('owner')) {
            throw new RuntimeException('owner for <' + test.scenario + '> is not set')
        }
    }
}
