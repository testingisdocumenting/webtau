package scenarios.ui

import static com.twosigma.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

scenario('search and capture') {
    search.submit("search this")

    browser.doc.withAnnotations(
            browser.doc.badge(search.box),
            browser.doc.badge(search.results)).capture('search')
}