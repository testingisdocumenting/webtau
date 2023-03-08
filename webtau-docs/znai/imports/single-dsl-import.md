# Single DSL Import

When you use multiple WebTau features you can single static import to have all core features available for autocomplete.
It also requires a single dependency to bring all the WebTau modules.

```tabs
Groovy:
:include-file: com/example/tests/junit5/WeatherGroovyTest.groovy {title: "core import", includeRegexp: "WebTauGroovyDsl"} 
:include-file: maven/groovy-dep.xml {title: "maven dependency"}

Java:
:include-file: com/example/tests/junit5/WeatherJavaTest.java {title: "core import", includeRegexp: "WebTauDsl"} 
:include-file: maven/java-dep.xml {title: "maven dependency"}
```
