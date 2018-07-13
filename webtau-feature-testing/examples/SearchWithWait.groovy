import static com.twosigma.webtau.WebTauGroovyDsl.scenario
import static pages.Pages.getSearch

scenario("""# Search facts
You can enter a fact in a search box and 
fact will be displayed in a special box
""") {
    search.open()
    search.submit(query: "search this")

    search.numberOfResults.waitTo == 2
}
