# Response Lazy Value

WebTau has a way to define a lazy value associated with `HTTP GET` response. After that it can be used in multiple tests, `should` and `waitTo` on it.

Value can be associated with a static url like `/info` or a dynamic url like `/price/:ticker`:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/http/HttpResourceGroovyTest.groovy {
  title: "wait on http response value",
  surroundedBy: ["live-price-definition", "live-price-wait"],
  surroundedBySeparator: "\n...\n"
}

Java:
:include-file: org/testingisdocumenting/webtau/http/HttpResourceJavaTest.java {
  title: "wait on http response value",
  surroundedBy: ["live-price-definition", "live-price-wait"],
  surroundedBySeparator: "\n...\n"
}
```

:include-cli-output: doc-artifacts/live-price-output.txt {title: "console output"}

# Complex Value Path

Value path supports multiple nesting level and arrays indexing:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/http/HttpResourceGroovyTest.groovy {
  title: "complex value path",
  surroundedBy: ["complex-path-definition", "complex-path-validation"],
  surroundedBySeparator: "\n...\n"
}

Java:
:include-file: org/testingisdocumenting/webtau/http/HttpResourceJavaTest.java {
  title: "complex value path",
  surroundedBy: ["complex-path-definition", "complex-path-validation"],
  surroundedBySeparator: "\n...\n"
}
```

:include-cli-output: doc-artifacts/complex-path-output.txt {
  title: "console output"
}

# Multiple URL Parameters

Use vararg, or pass a map to `of` to provide multiple URL parameters to your resource:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/http/HttpResourceGroovyTest.groovy {
  title: "multiple url params",
  surroundedBy: ["multiple-url-params-definition", "multiple-url-params-validation"],
  surroundedBySeparator: "\n...\n"
}

Java:
:include-file: org/testingisdocumenting/webtau/http/HttpResourceJavaTest.java {
  title: "multiple url params",
  surroundedBy: ["multiple-url-params-definition", "multiple-url-params-validation"],
  surroundedBySeparator: "\n...\n"
}
```

:include-cli-output: doc-artifacts/multiple-url-params-output.txt {
  title: "console output"
}
