package ui

import static com.twosigma.webtau.WebTauGroovyDsl.scenario
import static pages.Pages.getSearch

scenario("""Executes negated matcher against page element's underlying value""") {
    search.open()
    search.welcomeMessage.shouldNot == 'welcome to boring search'
}
