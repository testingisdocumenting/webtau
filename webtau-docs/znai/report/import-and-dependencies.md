:include-markdown: imports/single-dsl-import.md

# Core Only Import

Alternatively, you can only import `WebTauCore` for methods like `step`, `trace`, `warning`:

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/data/converters/ObjectPropertiesTest.groovy {title: "core import", includeRegexp: "WebTauCore"} 
:include-file: maven/core-dep-groovy.xml {title: "maven dependency"}

Java:
:include-file: com/example/tests/junit5/StepTraceJavaTest.java {title: "core import", includeRegexp: "WebTauCore"} 
:include-file: maven/core-dep.xml {title: "maven dependency"}
```
