# webtau

Web Test Automation [User Guide](https://opensource.twosigma.com/webtau/guide/)

![logo](webtau-docs/webtau/webtau-logo.png)

## Simple REST tests

### JUnit support

*Groovy*
```groovy
@RunWith(WebTauRunner.class)
class WeatherGroovyIT {
    @Test
    void checkWeather() {
        http.get("/weather") {
            temperature.shouldBe < 100
        }
    }
}
```

*Java*
```java
@RunWith(WebTauRunner.class)
public class WeatherJavaIT {
    @Test
    public void checkWeather() {
        http.get("/weather", (header, body) -> {
            body.get("temperature").shouldBe(lessThan(100));
        });
    }
}
```


### Groovy dev-ops friendly 

Support for command line friendly automation and exploration with Groovy specific simplified runner

```groovy
scenario("check weather") {
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

```
webtau weather.groovy
```

## Robust UI tests

```groovy
scenario("search by specific query") {
    search.submit("search this")
    search.numberOfResults.should == 2
}
```

## Precise Reporting

![report-image](report-crud-separated-http-calls.png)

[Full Documentation](https://opensource.twosigma.com/webtau/guide/)
