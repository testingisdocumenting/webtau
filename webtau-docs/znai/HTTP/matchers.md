---
type: two-sides
---

# Imports to use

:include-file: scenarios/rest/simpleGet.groovy {includeRegexp: "import.*Dsl", title: "Single import to use"}
 
# Response Mapping

Identifiers inside validation closure are automatically mapped to a response body.  
:include-empty-block: {rightSide: true}

:include-json: simpleObjectTestResponse.json {title: "object response", rightSide: false}

```tabs {rightSide: true}
Groovy: :include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "simple object mapping example", bodyOnly: true, excludeRegexp: "doc.capture"}
Java: :include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {entry: "simpleObjectMappingExample", bodyOnly: true, excludeRegexp: "doc.capture"}
```

List responses are handled by using index chain
:include-empty-block: {rightSide: true}

:include-json: simpleListTestResponse.json {title: "list response"}

```tabs {rightSide: true}
Groovy: 

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "simple list mapping example", bodyOnly: true, excludeRegexp: "doc.capture"}

 Note: Groovy API implicitly assumes `body`, but if you need to deal with array response you need to access values using `body` explicitly.

Java: :include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {entry: "simpleListMappingExample", bodyOnly: true, excludeRegexp: "doc.capture"}
```

# Should and Should Not

Matchers in webtau are triggered with `should` and `shouldNot` keywords.
Additionally `shouldBe` and `shouldNotBe` alias keywords are available to make certain matcher combinations easier to read.

```tabs {rightSide: true}
Groovy: :include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "matchers basic example", bodyOnly: true, excludeRegexp: "doc.capture"}
Java: :include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {entry: "matchersBasicExample", bodyOnly: true, excludeRegexp: "doc.capture"}
```

# Equality

Webtau defines its own set of equality rules to simplify testing. 
:include-empty-block: {rightSide: true}
 
:include-json: objectTestResponse.json {title: "response", pathsFile: "doc-artifacts/end-point-object-equality-matchers/paths.json"}

```tabs {rightSide: true}
Groovy: :include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "equality matcher", bodyOnly: true, commentsType: "inline", excludeRegexp: "doc.capture"}
Java: :include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {entry: "equalityMatcher", bodyOnly: true, commentsType: "inline", excludeRegexp: "doc.capture"}
```

```tabs {rightSide: true}
Groovy: :include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "equality matcher table keys", bodyOnly: true, commentsType: "inline", excludeRegexp: "doc.capture"}
Java: :include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {entry: "equalityMatcherTableKey", bodyOnly: true, commentsType: "inline", excludeRegexp: "doc.capture"}
```


# Greater/Less/Equal

Use `greaterThan`, `greaterThanOrEqual`, `lessThan`, and `lessThanOrEqual` to assert numeric values. 
:include-empty-block: {rightSide: true}

:include-json: numbersTestResponse.json {title: "response", pathsFile: "doc-artifacts/end-point-numbers-matchers/paths.json"}

```tabs {rightSide: true}
Groovy: :include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "compare numbers with greater less matchers", bodyOnly: true, excludeRegexp: "doc.capture"}
 
 Note: `Groovy` can use shortcuts `>`, `>=`, `<`, `<=`.

Java: :include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {entry: "compareNumbersWithGreaterLessMatchers", bodyOnly: true, excludeRegexp: "doc.capture"}
```

# Contain

Use `contain` when you cannot rely on order of values in a response. 
:include-empty-block: {rightSide: true}

:include-json: listTestResponse.json {title: "response", pathsFile: "doc-artifacts/end-point-list-contain-matchers/paths.json"}

```tabs {rightSide: true}
Groovy: :include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "contain matcher", bodyOnly: true, excludeRegexp: "doc.capture"}
Java: :include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {entry: "containMatcher", bodyOnly: true, excludeRegexp: "doc.capture"}
```

# Contain All

Use `containAll` when you cannot rely on order of values in a response and need to check more than one value. 
:include-empty-block: {rightSide: true}

:include-json: listTestResponse.json {title: "response", pathsFile: "doc-artifacts/end-point-list-contain-all-matchers/paths.json"}

```tabs {rightSide: true}
Groovy: :include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "contain all matcher", bodyOnly: true, excludeRegexp: "doc.capture"}
Java: :include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {entry: "containAllMatcher", bodyOnly: true, excludeRegexp: "doc.capture"}
```

# Nested Contain All

Use `containingAll` alias to make it easier to read `containAll` matcher nested inside `contain`.
:include-empty-block: {rightSide: true}

:include-json: prices.json {title: "response", pathsFile: "doc-artifacts/prices-contain-containing-all/paths.json"}

```tabs {rightSide: true}
Groovy: :include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "contain containing all matcher", bodyOnly: true, excludeRegexp: "doc.capture"}
Java: :include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {entry: "containContainingAllMatcher", bodyOnly: true, excludeRegexp: "doc.capture"}
```

# Date and Time

You can assert `actual` string against `LocalDate` and `ZonedDateTime`. String will be automatically converted 
using ISO formatter.
:include-empty-block: {rightSide: true}

:include-json: datesTestResponse.json {title: "response", pathsFile: "doc-artifacts/end-point-dates-matchers/paths.json"}

```tabs {rightSide: true}
Groovy: :include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "working with dates", bodyOnly: true, excludeRegexp: "doc.capture"}
Java: :include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {entry: "workingWithDates", bodyOnly: true, excludeRegexp: "doc.capture"}
```

# Mixing Matchers

You can use matchers in place of expected values to build a more complex expectation. 
:include-empty-block: {rightSide: true}

:include-json: mixedTestResponse.json {title: "response", pathsFile: "doc-artifacts/end-point-mixing-matchers/paths.json"}

```tabs {rightSide: true}
Groovy: :include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "matchers combo", bodyOnly: true, commentsType: "inline", excludeRegexp: "doc.capture"}
Java: :include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {entry: "matchersCombo", bodyOnly: true, commentsType: "inline", excludeRegexp: "doc.capture"}
```
