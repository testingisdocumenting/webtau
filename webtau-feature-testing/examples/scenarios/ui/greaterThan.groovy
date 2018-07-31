package scenarios.ui

import static com.twosigma.webtau.WebTauDsl.beGreaterThan
import static com.twosigma.webtau.WebTauGroovyDsl.scenario
import static pages.Pages.getSearch

scenario("greater than matcher") {
    search.open()

    search.submit(query: "search this")
    search.numberOfResults.waitTo beGreaterThan(1L)
}

