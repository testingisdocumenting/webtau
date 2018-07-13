import static com.twosigma.webtau.WebTauGroovyDsl.scenario
import static pages.Pages.getSearch

scenario("use `shouldNot` to negate any matcher") {
    search.open()
    search.submit(query: "search this")

    search.numberOfResults.shouldNot == 1
}
