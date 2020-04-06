# WebTau 

WebTau (**Web** **T**est **au**tomation) - concise and expressive way to create REST API and Web UI tests.

:include-image: webtau-logo.png {width: 256, align: "left"}


``````tabs

Groovy:
 ````columns

 left:
 :include-file: scenarios/rest/simpleGet.groovy {title: "REST API test (Groovy specific runner)"}
 :include-file:  com/example/tests/junit4/WeatherGroovyIT.groovy {title: "REST API test (JUnit4)"} 

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
:include-file:  com/example/tests/junit4/WeatherJavaIT.java {title: "REST API test (JUnit4 Java)"} 

 right: 
 ```json {title: "Server Response"}
 {
   "temperature": 88
 }
```
````

``````

Tests can be written in any JVM language. Language specific syntactic sugar is available for `Groovy`.

:include-file: scenarios/ui/searchWithPages.groovy {title: "Web UI test"}


