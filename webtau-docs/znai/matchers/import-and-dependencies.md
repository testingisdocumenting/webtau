:include-markdown: imports/single-dsl-import.md

# Matchers Specific Import

To specifically import `matchers` only use:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/expectation/equality/handlers/StringMatchersGroovyExamplesTest.groovy {title: "matchers import", include: "Matchers.*"}
:include-file: maven/core-dep.xml {title: "maven dependency"}

Java:

:include-file: org/testingisdocumenting/webtau/expectation/equality/handlers/StringMatchersJavaExamplesTest.java {title: "matchers import", include: "Matchers.*"} 
:include-file: maven/core-dep.xml {title: "maven dependency"}
```

# Core Module Import

To have one import for `matchers` and utilities like `list`, `map`, `table` use:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/MatchersTest.java { include: "WebTauCore.*", replace: [";", ""] }

Java:
:include-file: org/testingisdocumenting/webtau/MatchersTest.java { include: "WebTauCore.*" }
```

