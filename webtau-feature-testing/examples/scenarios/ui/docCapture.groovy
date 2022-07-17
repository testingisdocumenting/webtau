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

scenario("search and capture with badges shortcut") {
    search.submit("search this")

    browser.doc.withAnnotations(search.box, search.results).capture("search-with-shortcut")
}

scenario("search and capture results area") {
    search.submit("search this")

    browser.doc.withRoot(search.resultsArea)
            .withAnnotations(browser.doc.badge(search.results))
            .capture("search-results-area")
}

scenario("capture element outside of viewport") {
    search.submit("search this")
    browser.doc.withRoot($("#bottom")).capture("element-outside-viewport")
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

scenario("capture with rectangles") {
    browser.doc.withAnnotations(
            browser.doc.rect(search.box),
            browser.doc.rect(search.results, "covering text")).capture("search-rectangles")
}

scenario("capture with arrow") {
    browser.doc.withAnnotations(
            browser.doc.arrow(search.box, search.results, "search result")).capture("search-arrow")
}

scenario("capture not found element") {
    browser.doc.withAnnotations(
            browser.doc.badge($(".non-existing selector"))).capture("non-existing")
}