---
identifier: {validationPath: "org/testingisdocumenting/webtau/http/HttpResourceJavaTest.java"}
---

# Response Lazy Value

:include-java-doc: org/testingisdocumenting/webtau/http/Http.java { entry: "resource" }

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/http/HttpResourceGroovyTest.groovy {
  title: "wait on http response value",
  surroundedBy: ["live-price-definition", "live-price-wait"],
  surroundedBySeparator: "\n...\n"
}
:include-markdown: import-ref.md

Java:
:include-file: org/testingisdocumenting/webtau/http/HttpResourceJavaTest.java {
  title: "wait on http response value",
  surroundedBy: ["live-price-definition", "live-price-wait"],
  surroundedBySeparator: "\n...\n"
}
:include-markdown: import-ref.md
```

:include-cli-output: doc-artifacts/live-price-output.txt {title: "console output"}

# Full Body

Use `.body` to define a resource matching the whole response:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/http/HttpResourceGroovyTest.groovy {
  title: "wait on whole body",
  surroundedBy: ["no-path-definition", "no-path-wait"],
  surroundedBySeparator: "\n...\n"
}

Java:
:include-file: org/testingisdocumenting/webtau/http/HttpResourceJavaTest.java {
  title: "wait on whole body",
  surroundedBy: ["no-path-definition", "no-path-wait"],
  surroundedBySeparator: "\n...\n"
}
```

:include-cli-output: doc-artifacts/no-path-output.txt {title: "console output"}

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

# Full Text Response

Use `:identifier: fullTextResponse` to extract full original response:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/http/HttpResourceGroovyTest.groovy {
  title: "full text response",
  surroundedBy: "full-text-response"
}

Java:
:include-file: org/testingisdocumenting/webtau/http/HttpResourceJavaTest.java {
  title: "full text response",
  surroundedBy: "full-text-response"
}
```

# HTTP Header

Use `http.resource` optional second parameter to pass HTTP header:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/http/HttpResourceGroovyTest.groovy {
  title: "resource header",
  surroundedBy: "pass-header",
  replace: [["full-echo", "end-point"], ['get\\("x-prop"\\)', 'path']]
}

Java:
:include-file: org/testingisdocumenting/webtau/http/HttpResourceJavaTest.java {
  title: "resource header",
  surroundedBy: "pass-header",
  replace: [["full-echo", "end-point"], ['"x-prop"', '"path"']]
}
```
