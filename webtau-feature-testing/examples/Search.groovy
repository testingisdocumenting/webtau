import static com.twosigma.webtau.WebTauGroovyDsl.scenario
import static pages.Pages.getSearch

scenario("""# Search facts
Enter a fact in a search box and 
information will be displayed in a special box
""") {
    search.open()
    search.submit(query: "search this")

    search.numberOfResults.should == 2
}
