# Handler vs Matcher

Adding new equality and greater/less rules is done by creating and registering new `:identifier: CompareToHandler {validationPath: "org/testingisdocumenting/webtau/expectation/equality/CompareToHandler.java"}`.
Creating new matchers is not required in this case. 

# CompareToHandler {style: "api"}

To add a new handler you need to
* implement `:identifier: CompareToHandler {validationPath: "org/testingisdocumenting/webtau/expectation/equality/CompareToHandler.java"}`
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