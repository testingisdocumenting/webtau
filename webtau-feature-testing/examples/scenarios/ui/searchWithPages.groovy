package scenarios.ui

import static com.twosigma.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

scenario("search by specific query") {
    search.submit("search this")
    search.numberOfResults.should == 2
}
