import static com.twosigma.webtau.WebTauGroovyDsl.scenario
import static pages.Pages.getSearch

scenario("""# Alternative Search
to do different description
""") {
    search.open()
    search.box.should == "search this1"
}

scenario("""# Alternative Mega Search""") {
    search.open()
}
