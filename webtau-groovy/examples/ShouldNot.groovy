import static com.twosigma.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

scenario("use `shouldNot` to negate any matcher") {
    search.open()
    search.submit(query: "search this")

    search.numberOfResults.shouldNot == 1
}
