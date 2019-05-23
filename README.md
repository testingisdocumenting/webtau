# webtau

Web Test Automation [User Guide](https://opensource.twosigma.com/webtau/guide/)

![logo](webtau-docs/webtau/webtau-logo.png)

## Simple REST tests

```groovy
scenario("simple get") {
    http.get("/weather") {
        temperature.shouldBe < 100
    }
}
```
```json
{
  "temperature": 88
}
```

## Robust UI tests

```groovy
scenario("search by specific query") {
    search.submit("search this")
    search.numberOfResults.should == 2
}
```

## Precise Reporting

![report-image](webtau-report-testing/src/test/groovy/doc-artifacts/reports/report-http-calls.png)

[Full Documentation](https://opensource.twosigma.com/webtau/guide/)
