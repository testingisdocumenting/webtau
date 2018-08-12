package scenarios.ui

import static com.twosigma.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

scenario("""Executes passed matcher against page element's underlying value""") {
    search.open()
    search.welcomeMessage.should == 'welcome to super search'
}

