package scenarios.ui

import static com.twosigma.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

scenario("greater than matcher") {
    search.open()

    search.submit(query: "search this")
    search.numberOfResults.waitToBe greaterThan(1L)
}

