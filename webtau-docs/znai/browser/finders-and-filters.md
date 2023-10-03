---
type: two-sides
---

# Finders

Finders in WebTau is the initial web element selection that could select one or more elements.

# CSS Finder

Use `$` or `browser.element` to select an element by a given `css` selector ([Read W3Schools CSS selectors](https://www.w3schools.com/cssref/css_selectors.asp) to learn all kind of CSS selection techniques).   

:include-file: doc-artifacts/snippets/finders-filters/byCss.groovy {title: "example of selecting by id"}

If more than one element is matched, the first one will be used for actions and assertions.

:include-file: doc-artifacts/snippets/finders-filters/byCssFirstMatched.groovy {title: "example of selecting by nested tags"}

While `click` and `sendKeys` will always work on a first element only, the matchers can work with a list of things.

:include-file: doc-artifacts/snippets/finders-filters/byCssAllMatched.groovy

Note: declaring element this way will not trigger element search right away.

:include-meta: {rightSide: true}

:include-empty-block:

:include-xml: doc-artifacts/snippets/finders-filters/welcome.html {paths: ["div"]}

:include-xml: doc-artifacts/snippets/finders-filters/flat-menu.html {paths: ["ul.li[0].a"]}

:include-xml: doc-artifacts/snippets/finders-filters/flat-menu.html {paths: ["ul.li[0].a", "ul.li[1].a", "ul.li[2].a"]}

# Basic Filters

You can use filters to narrow down elements selected by finders.

Use `get` method to filter found elements by
* Element number
* Element text
* Element regexp 

:include-file: doc-artifacts/snippets/finders-filters/byCssAndFilterByNumber.groovy { title: "number" }
:include-file: doc-artifacts/snippets/finders-filters/byCssAndFilterByText.groovy { title: "text" }
:include-file: doc-artifacts/snippets/finders-filters/byCssAndFilterByRegexp.groovy { title: "regexp" }

:include-xml: doc-artifacts/snippets/finders-filters/flat-menu.html {rightSide: true, paths: ["ul.li[1].a"]}

# Filter By Distance

:include-file: doc-artifacts/snippets/finders-filters/byGeometry.groovy { title: "nearby filter", surroundedBy: "nearby-example" }
:include-xml: doc-artifacts/snippets/finders-filters/relative-selections.html {rightSide: true, paths: ["div.button[2]"]}

# Chaining

After you filtered, you can use finders again to find nested elements.

:include-file: doc-artifacts/snippets/finders-filters/byCssAndFilterByNumberNestedFind.groovy { title: "nested css selector" }

:include-xml: doc-artifacts/snippets/finders-filters/flat-menu.html {rightSide: true, paths: ["ul.li[1].a"]}

# Parent Finder

:include-file: doc-artifacts/snippets/finders-filters/byParent.groovy {title: "select immediate parent", highlight: "parent"}

:include-meta: {rightSide: true}

:include-empty-block:

:include-xml: doc-artifacts/snippets/finders-filters/welcome-wrapper.html 

# Parent Finder By CSS

:include-file: doc-artifacts/snippets/finders-filters/byParentCss.groovy {title: "select parent by css", highlight: "parent"}

:include-meta: {rightSide: true}

:include-empty-block:

:include-xml: doc-artifacts/snippets/finders-filters/welcome-wrapper-wrapper.html 
