package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

scenario("search by specific query") {
    search.submit("search this")
    search.numberOfResults.waitTo == 2
}
