# Mismatch Pinpoint Single Line Text

When comparing strings, WebTau highlights a specific point of failure:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/expectation/equality/handlers/StringMatchersGroovyExamplesTest.groovy {
  surroundedBy: "single-line-compare"
}
:include-markdown: import-ref.md

Java:
:include-file: org/testingisdocumenting/webtau/expectation/equality/handlers/StringMatchersJavaExamplesTest.java {
  surroundedBy: "single-line-compare"
}
:include-markdown: import-ref.md
```

:include-cli-output: doc-artifacts/single-line-string-compare-output.txt {
  title: "console output"
}

# Mismatch Pinpoint Multi Line Text

In case of a multiline text, WebTau highlights a first mismatched line:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/expectation/equality/handlers/StringMatchersGroovyExamplesTest.groovy {
  surroundedBy: "multi-line-compare"
}

Java:
:include-file: org/testingisdocumenting/webtau/expectation/equality/handlers/StringMatchersJavaExamplesTest.java {
  surroundedBy: "multi-line-compare"
}
```

:include-cli-output: doc-artifacts/multi-line-string-compare-output.txt {
  title: "console output"
}

# Line Ending Detection

WebTau automatically checks line ending, e.g. presence of `\r` and will notify if there is a difference.

:include-cli-output: doc-artifacts/line-ending-string-compare-output.txt {
  title: "console output"
}

# Different Number Of Empty Lines

WebTau separately checks extra empty lines to help with missing/extra `\n`:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/expectation/equality/handlers/StringMatchersGroovyExamplesTest.groovy {
  surroundedBy: "extra-empty-line-compare"
}

Java:
:include-file: org/testingisdocumenting/webtau/expectation/equality/handlers/StringMatchersJavaExamplesTest.java {
  surroundedBy: "extra-empty-line-compare"
}
```

:include-cli-output: doc-artifacts/extra-empty-line-string-compare-output.txt {
  title: "console output"
}

# Contains

Use `contains` matcher to check a presence of a substring:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/expectation/equality/handlers/StringMatchersGroovyExamplesTest.groovy {
  surroundedBy: "multi-line-contains"
}

Java:
:include-file: org/testingisdocumenting/webtau/expectation/equality/handlers/StringMatchersJavaExamplesTest.java {
  surroundedBy: "multi-line-contains"
}
```

:include-cli-output: doc-artifacts/string-contains-output.txt {
  title: "console output"
}

# Regexp

Use `equal` matcher to match against a regular expression:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/expectation/equality/handlers/StringMatchersGroovyExamplesTest.groovy {
  surroundedBy: "single-line-regexp"
}

Java:
:include-file: org/testingisdocumenting/webtau/expectation/equality/handlers/StringMatchersJavaExamplesTest.java {
  surroundedBy: "single-line-regexp"
}
```

:include-cli-output: doc-artifacts/string-regexp-output.txt {
  title: "console output"
}



