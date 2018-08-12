package scenarios.ui

import static com.twosigma.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

scenario("search by specific query") {
    search.submit(query: "search this")
    search.numberOfResults.waitTo == 2
}
