package scenarios.ui


import static com.twosigma.webtau.WebTauGroovyDsl.scenario
import static pages.Pages.getSearch

scenario("search by specific query") {
    search.submit("search this")
    search.numberOfResults.should == 2
}
