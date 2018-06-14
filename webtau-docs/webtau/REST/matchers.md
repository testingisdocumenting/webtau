---
type: two-sides
---

# Equality

Identifiers inside validation closure are automatically mapped to a response body. So typing 

```groovy
id.shouldNot == null 
``` 

will be automatically mapped to response `body.id`. If you need to access body explicitly use 

```groovy
body.shouldNot == [1, 2, 3] 
``` 

:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {entry: "equality matcher", bodyOnly: true, commentsType: "inline"}

:include-meta: {rightSide: true}
:include-json: objectTestResponse.json {title: "response", pathsFile: "doc-artifacts/end-point-object-equality-matchers/paths.json"}

# Greater/Less/Equal

:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {entry: "compare numbers with greater less matchers", bodyOnly: true}

:include-meta: {rightSide: true}
:include-json: numbersTestResponse.json {title: "response", pathsFile: "doc-artifacts/end-point-numbers-matchers/paths.json"}

# Contain

:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {entry: "contain matcher", bodyOnly: true}

:include-meta: {rightSide: true}
:include-json: listTestResponse.json {title: "response", pathsFile: "doc-artifacts/end-point-list-contain-matchers/paths.json"}

# Date and Time

You can assert `actual` string against `LocalDate` and `ZonedDateTime`. String will be automatically converted 
using ISO formatter.

:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {entry: "working with dates", bodyOnly: true}

:include-meta: {rightSide: true}
:include-json: datesTestResponse.json {title: "response", pathsFile: "doc-artifacts/end-point-dates-matchers/paths.json"}

# Mixing Matchers

You can use matchers in place of expected values to build a more complex expectation. 

:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {entry: "matchers combo", bodyOnly: true, commentsType: "inline"}

Note: names `greaterThan` and `beGreaterThan` are interchangeable. For better readability use `greaterThan` version when mixing matchers and `beGreaterThan` as 
a top level matcher.  

:include-meta: {rightSide: true}
:include-json: mixedTestResponse.json {title: "response", pathsFile: "doc-artifacts/end-point-mixing-matchers/paths.json"}

