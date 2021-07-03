package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

scenario("search and fail assertion") {
    search.submit("search this")
    search.numberOfResults.should == 2
}
