# WebTau 

WebTau (**Web** **T**est **au**tomation) - concise and expressive way to create REST API and Web UI tests.

:include-image: webtau-logo.png {width: 256, align: "left"}

````columns

left: :include-file: scenarios/rest/simpleGet.groovy {title: "REST API test"}
right: 
```json {title: "Server Response"}
{
  "temperature": 88
}
```
````

Tests can be written in any JVM languages with language specific syntactic sugar where applicable.

:include-file: scenarios/ui/searchWithPages.groovy {title: "Web UI test"}


