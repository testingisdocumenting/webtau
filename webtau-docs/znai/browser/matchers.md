---
type: two-sides
---

# Text

:include-file: doc-artifacts/snippets/matchers/equalText.groovy {title: "exact text"}

:include-file: doc-artifacts/snippets/matchers/equalTextRegexp.groovy {title: "regexp text"}

:include-file: doc-artifacts/snippets/matchers/equalListOfText.groovy {title: "list of text"}

:include-file: doc-artifacts/snippets/matchers/containTextInFirstElement.groovy {title: "contain text in the first matched element"}

:include-file: doc-artifacts/snippets/matchers/containTextInList.groovy {title: "contain exact text in list of elements"}

Note: `all()` is used at element declaration time to disambiguate between contain text in the first element and in the list of elements.
It is not required when you do an explicit comparison with the list on the right, since then webtau can deduce the desired outcome.

:include-file: doc-artifacts/snippets/matchers/equalListOfTextAndRegexp.groovy {title: "list of text and regexp"}

:include-xml: doc-artifacts/snippets/matchers/texts.html {rightSide: true}

# Numbers

:include-file: doc-artifacts/snippets/matchers/equalNumber.groovy {title: "exact number"}

:include-file: doc-artifacts/snippets/matchers/greaterNumber.groovy {title: "greater than"}

:include-file: doc-artifacts/snippets/matchers/greaterEqualNumber.groovy {title: "greater than or equal"}

:include-file: doc-artifacts/snippets/matchers/equalListOfNumbers.groovy {title: "list of numbers"}

:include-file: doc-artifacts/snippets/matchers/lessEqualListMixOfNumbers.groovy {title: "list of exact and greater/less"}

:include-xml: doc-artifacts/snippets/matchers/numbers.html {rightSide: true}

# State

:include-file: doc-artifacts/snippets/matchers/enabledDisabled.groovy {title: "enabled/disabled"}

:include-file: doc-artifacts/snippets/matchers/visibleHidden.groovy {title: "visible/hidden"}

:include-file: doc-artifacts/snippets/matchers/state.html {rightSide: true}

