package listeners

import com.twosigma.webtau.reporter.TestListener
import com.twosigma.webtau.reporter.WebTauTest

class MetaDataValidationTestListener implements TestListener {
    @Override
    void afterLastTestStatement(WebTauTest test) {
        if (!test.metadata.has('owner')) {
            throw new RuntimeException('owner for <' + test.scenario + '> is not set')
        }
    }
}
