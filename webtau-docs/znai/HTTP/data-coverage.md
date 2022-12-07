# Code Coverage VS Data Coverage

Code Coverage helps to reveal what paths of business logic you forgot to exercise.
But it doesn't help as much with HTTP API testing: a single call to an API may exercise the entire code path.

One of the main artifacts of HTTP API testing is a response. Does response have correct data? Are there data paths that we never asserted on?

WebTau looks across all the executed tests and called HTTP API methods to track what calls have response fields that were never observed. 
At the end report provides **Data Coverage** you can act on: 
* Remove fields from the response
* Add an extra assertion

# Detect Fields That Were Skipped

Let's consider an end-point that returns a weather temperature in Fahrenheit and Celsius. And Later we decided to add `time` field to the response.

:include-json: new-york-weather.json {title: "server response", paths: "root.time"}

Our tests were asserting on both temperature fields, but we have no assertions on `time` field.

```tabs
Groovy:
:include-file: scenarios/rest/coverage/newYorkWeather.groovy { title: "New York weather test" }

Java:
:include-file:  com/example/tests/junit5/NewYorkWeatherJavaTest.java { title: "New York weather test" } 
```
    
When we run our test suite, we will see in the output that both temperatures were asserted (`~~` in the response). 
But since we didn't touch `time` field, at the end of test run, WebTau will print a warning about missing validation

:include-cli-output: doc-artifacts/com.example.tests.junit5.NewYorkWeatherJavaTest-console-output.txt {
  highlight: ["~~88~~", "~~31~~", "Data Coverage"],
  excludeRegexp: ["Total time", "Total: ", "report is generated"]
}

WebTau prints only the first three routes with the skipped fields, and only first three fields.
More details is available in the produced HTML report.

:include-image: doc-artifacts/reports/http-data-coverage-report.png {annotate: true, border: true}

1. HTTP Data Coverage leads to more details on Routes that have fields that were skipped
2. Expanding a Route will list all the ignored fields

# Routing

WebTau needs to know how to group URLs you call. In the example above `/city/NewYork` and `/city/London` belongs to the same group.
And if you validate Celsius field when calling NewYork and Fahrenheit when calling London, fields will be considered as covered.

This is because WebTau identifies both URLs as `/city/:id`

:include-cli-output: doc-artifacts/com.example.tests.junit5.NewYorkWeatherJavaTest-console-output.txt { 
  highlight: ["reading", "mapped"],
  startLine: "checkFahrenheitTemperature",
  endLine: "temperatureF"
}
 
There are two ways to provide routing information:
1. Provide [Open API](HTTP/openAPI-spec) spec
2. Provide http routes text files

# Text Routes

If you don't have [Open API](HTTP/openAPI-spec) spec, you can define a plain text files that enumerates your API routes like this

:include-file: src/test/resources/data/http-routes.txt {autoTitle: true}

Note: variant with curly brackets for parameters also works `/city/{id}`

Specify file path (or resource for java) in your config file to enable it

```tabs
Groovy:  
:include-file: scenarios/rest/coverage/textRoutes.cfg.groovy {title: "webtau.cfg.groovy", includeRegexp: "httpRoutesPath"}

Java:
:include-file: src/test/resources/webtau.routes.properties {title: "webtau.properties", includeRegexp: "httpRoutesPath"}
```
