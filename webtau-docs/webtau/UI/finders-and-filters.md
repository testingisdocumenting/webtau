---
type: two-sides
---

# Finders

Finders in webtau is the initial web element selection that could select one or more elements.

# By CSS

Use `$` to select an element by a given `css` selector

:include-file: doc-artifacts/snippets/finders-filters/byCss.groovy

If more than one element is matched, the first one will be used for actions and assertions.

:include-file: doc-artifacts/snippets/finders-filters/byCssFirstMatched.groovy

While `click` and `sendKeys` will always work on a first element only, the matchers can work with a list of things.

:include-file: doc-artifacts/snippets/finders-filters/byCssAllMatched.groovy

:include-meta: {rightSide: true}
:include-xml: doc-artifacts/snippets/finders-and-filters-flat-menu.html {paths: ["div.div[0]", "div.div[1].ul.li[0].a"]}

# Filters

You can use filters to narrow down elements selected by finders like `css`.

Filter comes in a way of `get` method. Parameter is one of the following 
* Element number
* Element text
* Element regexp 

:include-file: doc-artifacts/snippets/finders-filters/byCssAndFilterByNumber.groovy { title: "Number" }
:include-file: doc-artifacts/snippets/finders-filters/byCssAndFilterByText.groovy { title: "Text" }
:include-file: doc-artifacts/snippets/finders-filters/byCssAndFilterByRegexp.groovy { title: "Regexp" }

:include-meta: {rightSide: true}
:include-xml: doc-artifacts/snippets/finders-and-filters-flat-menu.html {paths: ["div.div[1].ul.li[1].a"]}
