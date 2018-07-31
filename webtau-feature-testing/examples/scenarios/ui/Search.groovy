package scenarios.ui

import pages.Pages

import static com.twosigma.webtau.WebTauGroovyDsl.scenario

scenario("search by specific query") {
    Pages.search.open()
    Pages.search.submit(query: "search this")

    Pages.search.numberOfResults.should == 2
}
