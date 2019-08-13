search.submit("search this")

browser.doc.withAnnotations(
        browser.doc.badge(search.box),
        browser.doc.badge(search.results)).capture('search')