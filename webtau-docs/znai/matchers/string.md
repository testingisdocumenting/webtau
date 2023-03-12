# Mismatch Pinpoint Single Line Text

When comparing strings, WebTau highlights a specific point of failure:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/expectation/equality/handlers/StringMatchersGroovyExamplesTest.groovy {
  surroundedBy: "single-line-compare"
}

Java:
:include-file: org/testingisdocumenting/webtau/expectation/equality/handlers/StringMatchersJavaExamplesTest.java {
  surroundedBy: "single-line-compare"
}
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

