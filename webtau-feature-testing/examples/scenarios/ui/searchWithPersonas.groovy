package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauDsl.browser
import static org.testingisdocumenting.webtau.WebTauGroovyDsl.scenario
import static pages.Pages.search
import static personas.Personas.John

scenario("multiple browsers for search") {
    search.submit("search this")
    search.numberOfResults.should == 3

    John {
        search.submit("or that")
        search.numberOfResults.should == 3
    }
    
    search.box.should == "search this"

    John {
        search.box.should == "or that"
    }

    John {
        browser.close()
        search.submit('new search')
    }

    search.box.should == "search this"
}
