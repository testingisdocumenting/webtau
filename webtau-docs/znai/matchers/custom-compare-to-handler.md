---
identifier: {validationPath: "org/testingisdocumenting/webtau/expectation/equality/CompareToHandler.java"}`
---

# Handler vs Matcher

A lot of times you need to check if two things are equal.
Instead of creating multiple matchers like `arraysEqual`, `pathsEqual`, `myDataEqual`, you define additional equality rule.

Adding a new equality and greater/less rules is done by creating and registering new `:identifier: CompareToHandler`.

# CompareToHandler {style: "api"}

To add a new handler you need to
* implement `:identifier: CompareToHandler`
* register new class using Java Services 

## Interface 

:include-java: org/testingisdocumenting/webtau/expectation/equality/CompareToHandler.java {
    title: "interface to implement",
    entry: "CompareToHandler"
}

## Example Implementation

Below is an existing implementation of handler to deal with Java Bean as actual and Map as expected

:include-java: org/testingisdocumenting/webtau/expectation/equality/handlers/MapAndBeanCompareToHandler.java {
    title: "map and bean comparison handler",
    entry: "MapAndBeanCompareToHandler",
    commentsType: "inline"
}

## Registration

WebTau uses [Service Loaders](https://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html) to discover implementations of handlers.
To register an additional handler you need to create a file under resources 

```
META-INF/services/org.testingisdocumenting.webtau.expectation.equality.CompareToHandler
```

with the content file containing a line per implementing class

:include-file: src/main/resources/META-INF/services/org.testingisdocumenting.webtau.expectation.equality.CompareToHandler {
    autoTitle: true,
    includeRegexp: "MapAndBeanCompareToHandler" }

# Custom Complex Domain Data

Another example to use custom handler is to provide better mismatch details. 
Imagine you have some `CustomComplexData` class that contains important data from you domain. 

This class also has `equals` defined, and you can use it to check if two values are the same.
But when it comes to use it during testing, you find that failure report is hard to comprehend: default assertion 
only prints `toString` representation leaving you to eyeball the rest.

:include-java: org/example/domain/CustomComplexDataTest.java {
  title: "default assertion example",
  surroundedBy: ["complex-data-fail-preparation", "default-assertion"],
  surroundedBySeparator: ""
}

:include-cli-output: doc-artifacts/custom-complex-data-default-fail-output.txt { title: "console output" }

To improve the situation, we will define `CustomComplexData` `:identifier: CompareToHandler`:

:include-file: org/example/domain/CustomComplexDataCompareToHandler.java { 
  autoTitle: true,
  startLine: "package",
  commentsType: "inline"
}

Once handler is registered, additional information will be printed during mismatch:

:include-file: src/test/resources/META-INF/services/org.testingisdocumenting.webtau.expectation.equality.CompareToHandler {
  autoTitle: true,
  includeRegexp: "CustomComplex" 
}

:include-java: org/example/domain/CustomComplexDataTest.java {
  title: "webtau assertion example",
  surroundedBy: ["complex-data-fail-preparation", "webtau-assertion"],
  surroundedBySeparator: ""
}

:include-cli-output: doc-artifacts/custom-complex-data-webtau-fail-output.txt { title: "console output" }
