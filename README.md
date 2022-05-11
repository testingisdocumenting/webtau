![build](https://github.com/testingisdocumenting/webtau/workflows/Build%20webtau/badge.svg)

# Webtau

Web Test Automation [User Guide](https://testingisdocumenting.org/webtau/)

![logo](webtau-docs/znai/webtau-logo.png)

Webtau (**Web** **T**est **au**tomation) - concise and expressive way to write end-to-end and unit tests.

Test your application across multiple layers:
* REST API
* GraphQL API
* Web UI
* CLI
* Database
* Business Logic (JVM only)

[REST test Groovy example](https://testingisdocumenting.org/webtau/HTTP/introduction):
```groovy
scenario("check weather") {
    http.get("/weather") {
        temperature.shouldBe < 100
    }
}
```

Use one layer to re-enforce tests on another. E.g. REST API layer to set up data for Web UI test, or database layer
to validate GraphQL API.

Use REPL to tighten test feedback loop and speed up test writing
```groovy
webtau:000> $("ul li a")
element is found: by css ul li a
           getText(): Guide
getUnderlyingValue(): Guide
               count: 3
```

Capture test artifacts like API Responses, screenshots, command line output to automate your user facing documentation.

Leverage out of the box rich reporting:
![report example](https://testingisdocumenting.org/webtau/doc-artifacts/reports/report-crud-separated-http-calls.png)

Tests can be written in any JVM language. Language specific syntactic sugar is available for `Groovy`.

* [Full User Guide](https://testingisdocumenting.org/webtau/)
* [Multiple layers testing example blog](https://testingisdocumenting.org/blog/entry/ultimate-end-to-end-test)

--------

[Browser test Java example](https://testingisdocumenting.org/webtau/browser/introduction):
```java
@WebTau
public class WebSearchTest {
    @Test
    public void searchByQuery() {
        search.submit("search this");
        search.numberOfResults.waitToBe(greaterThan(1));
    }
}

public class SearchPage {
    private final PageElement box = $("#search-box");
    private final PageElement results = $("#results .result");
    public final ElementValue<Integer> numberOfResults = results.getCount();

    public void submit(String query) {
        browser.open("/search");

        box.setValue(query);
        box.sendKeys(browser.keys.enter);
    }
}
```

[GraphQL example](https://testingisdocumenting.org/webtau/GraphQL/introduction):
```groovy
@Webtau
public class GraphQLWeatherJavaIT {
    @Test
    public void checkWeather() {
        String query = "{ weather { temperature } }";
        graphql.execute(query, (header, body) -> {
            body.get("data.weather.temperature").shouldBe(lessThan(100));
        });
    }
}
```

[Database data setup example](https://testingisdocumenting.org/webtau/database/data-setup):
```groovy
def PRICES = db.table("PRICES")
PRICES << [     "id" | "description" |          "available" |                "type" |       "price" ] {
           _____________________________________________________________________________________________
           cell.guid | "nice set"    |                 true |                "card" |            1000
           cell.guid | "nice set"    |                 true |                "card" | cell.above + 10
           cell.guid | "another set" | permute(true, false) | permute("rts", "fps") | cell.above + 20 }
```

[CLI run example](https://testingisdocumenting.org/webtau/cli/introduction):
```groovy
cli.run('echo hello world') {
    output.should contain('hello')
    output.should contain('world')
}
```

[Learn More](https://testingisdocumenting.org/webtau/)
