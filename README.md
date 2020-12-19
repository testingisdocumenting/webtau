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

Use one layer to re-enforce tests on another. E.g. REST API layer to set up data for Web UI test, or database layer
to validate GraphQL API.

Tests can be written in any JVM language. Language specific syntactic sugar is available for `Groovy`.

```groovy
scenario("check weather") {
    http.get("/weather") {
        temperature.shouldBe < 100
    }
}
```

```
def PRICES = db.table("PRICES")
PRICES << [     "id" | "description" |          "available" |                "type" |       "price" ] {
           _____________________________________________________________________________________________
           cell.guid | "nice set"    |                 true |                "card" |            1000
           cell.guid | "nice set"    |                 true |                "card" | cell.above + 10
           cell.guid | "another set" | permute(true, false) | permute("rts", "fps") | cell.above + 20 }
```

[Learn More](https://testingisdocumenting.org/webtau/)
