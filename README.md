# webtau

Web Test Automation [User Guide](https://opensource.twosigma.com/webtau/guide/)

Simple REST tests

```groovy
scenario("simple get") {
    http.get("/weather") {
        temperature.should == 88
    }
}
```

Robust UI test

```groovy
scenario("""# Search facts
Enter a fact in a search box and 
information will be displayed in a special box
""") {
    search.open()
    search.submit(query: "search this")

    search.numberOfResults.should == 2
}
```
