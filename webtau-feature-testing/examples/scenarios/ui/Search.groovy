package scenarios.ui

import static com.twosigma.webtau.WebTauGroovyDsl.scenario
import static pages.Pages.search

scenario("search by specific query") {
    search.open()
    search.submit(query: "search this")

    search.numberOfResults.should == 2
}
