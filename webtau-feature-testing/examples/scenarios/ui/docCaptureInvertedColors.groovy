package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

scenario('badge with inverted colors from config') {
    search.submit("search this")

    // example-start
    browser.doc.withAnnotations(
            browser.doc.badge(search.header),
            browser.doc.badge(search.results).invertedColors()).capture('search-inverted-colors-from-config')
    // example-end
    data.json.map('doc-artifacts/search-inverted-colors-from-config.json')
            .shapes.darkFriendly.should == [true, false]
}
