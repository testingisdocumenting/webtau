package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

scenario("restart browser to use a config size") {
    browser.restart()
}

scenario("search and capture with badges") {
    search.submit("search this")

    browser.doc.withAnnotations(
            browser.doc.badge(search.box),
            browser.doc.badge(search.results)).capture("search")
}

scenario("search and capture results area") {
    search.submit("search this")

    browser.doc.withRoot(search.resultsArea)
            .withAnnotations(browser.doc.badge(search.results))
            .capture("search-results-area")
}

scenario("search and capture with badges placed in non center position") {
    search.submit("search this")

    browser.doc.withAnnotations(
            browser.doc.badge(search.box).toTheRight(),
            browser.doc.badge(search.results).above()).capture("search-diff-placement")
    // example-end
    data.json.map("doc-artifacts/search-diff-placement.json")
            .shapes.align.should == ["ToTheRight", "Above"]
}

scenario("badge with inverted colors") {
    search.submit("search this")

    browser.doc.withAnnotations(
            browser.doc.badge(search.header).invertedColors(),
            browser.doc.badge(search.box)).capture("search-inverted-colors")
    // example-end
    data.json.map("doc-artifacts/search-inverted-colors.json")
            .shapes.darkFriendly.should == [true, false]
}

scenario("capture with highlight and cover") {
    browser.doc.withAnnotations(
            browser.doc.highlight(search.box),
            browser.doc.cover(search.results, "covering text")).capture("search-highlight-cover")
}

scenario("capture with arrow") {
    browser.doc.withAnnotations(
            browser.doc.arrow(search.box, "type to search")).capture("search-arrow")
}

scenario("capture not found element") {
    browser.doc.withAnnotations(
            browser.doc.badge($(".non-existing selector"))).capture("non-existing")
}