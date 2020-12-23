Webtau `http.` module lets you exercise and validate HTTP endpoints.
It provides a simplified way to access JSON response of an end-point and simplifies REST API tests.

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
 