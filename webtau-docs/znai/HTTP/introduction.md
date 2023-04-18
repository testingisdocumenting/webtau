WebTau `http` module lets you exercise and validate HTTP endpoints.
It provides a simplified way to make HTTP calls and validate responses

Now with [Data Coverage](HTTP/data-coverage).

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

``````tabs
Groovy:
 ````columns

 left:
 :include-file: scenarios/rest/simpleGet.groovy {title: "REST API test (Groovy specific runner)"}
 :include-file: com/example/tests/junit5/WeatherGroovyTest.groovy {title: "REST API test (JUnit5)"} 

 right: 
 ```json {title: "Server Response"}
 {
   "temperature": 88
 }
```
````
Java:
 ````columns

 left:
:include-file:  com/example/tests/junit5/WeatherJavaTest.java {title: "REST API test (JUnit5 Java)"} 

 right: 
 ```json {title: "Server Response"}
 {
   "temperature": 88
 }
```
````
``````

:include-cli-output: doc-artifacts/com.example.tests.junit5.WeatherGroovyTest-console-output.txt {
  title: "console output",
  startLine: "executing",
  endLine: "executed"
}
