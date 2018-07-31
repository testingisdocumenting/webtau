# webtau

Web Test Automation [User Guide](https://opensource.twosigma.com/webtau/guide/)

## Simple REST tests

```groovy
scenario("simple get") {
    http.get("/weather") {
        temperature.shouldBe < 100
    }
}
```

## Robust UI tests

```groovy
scenario("search by specific query") {
    search.open()
    search.submit(query: "search this")

    search.numberOfResults.should == 2
}
```

## Precise Reporting

![report-image](webtau-docs/webtau/img/rest-crud-separated-report.png)
