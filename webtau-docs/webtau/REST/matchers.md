---
type: two-sides
---

# Response Mapping

Identifiers inside validation closure are automatically mapped to a response body.  

:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {entry: "simple object mapping example", bodyOnly: true}

:include-meta: {rightSide: true}
:include-json: simpleObjectTestResponse.json {title: "object response"}

:include-meta: {rightSide: false}

In case of an array response you need to access values using `body`.

:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {entry: "simple list mapping example", bodyOnly: true}

:include-meta: {rightSide: true}
:include-json: simpleListTestResponse.json {title: "list response"}

# Should and Should Not

Matchers in webtau are triggered with `should` and `shouldNot` keywords.

```groovy
myValue.should contain(10)
myValue.shouldNot equal("hello")
```  

Some matchers have alternative shortcuts.
 
```groovy
myValue.should != 10
```  

Additionally `shouldBe` and `shouldNotBe` alias keywords are available to make certain matcher combinations easier to read
 
```groovy
myValue.shouldBe greaterThan(10)
myValue.shouldBe > 10
```  

# Equality

Webtau defines its own set of equality rules to simplify testing. 
 
:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {entry: "equality matcher", bodyOnly: true, commentsType: "inline"}

:include-meta: {rightSide: true}
:include-json: objectTestResponse.json {title: "response", pathsFile: "doc-artifacts/end-point-object-equality-matchers/paths.json"}

# Greater/Less/Equal

Standard operations `>`, `>=`, `<`, `<=` are supported as shortcuts for `greaterThan`, `greaterThanOrEqual`, `lessThan`, and `lessThanOrEqual`.

:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {entry: "compare numbers with greater less matchers", bodyOnly: true}

:include-meta: {rightSide: true}
:include-json: numbersTestResponse.json {title: "response", pathsFile: "doc-artifacts/end-point-numbers-matchers/paths.json"}

# Contain

Use `contain` when you cannot rely on order. 

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

:include-meta: {rightSide: true}
:include-json: mixedTestResponse.json {title: "response", pathsFile: "doc-artifacts/end-point-mixing-matchers/paths.json"}

