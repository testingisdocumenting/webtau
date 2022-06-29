Here we cover essential configuration for HTTP testing.

To learn basics about configuration, head over to [Configuration Getting Started](getting-started/configuration).
\
To learn about all configuration capabilities, head over to [Configuration page](configuration/groovy-config-file). 

# Base URL {style: "api"}

Use `url` to set base url for your tests.

```tabs
Groovy:

:include-file: scenarios/rest/urlOnly.cfg.groovy {
  title: "webtau.cfg.groovy",
  includeRegexp: "localhost"
}

 Note: WebTau treats groovy config file as code

Java:

When you use JUnit like runners, e.g. [JUnit5](getting-started/installation#junit5), WebTau expects file named
`webtau.properties` to be present in test classpath, e.g. test resources:

:include-file: webtau-junit5-examples/src/test/resources/webtau.properties {
  title: "src/test/resources/webtau.properties",
  includeRegexp: "localhost"
}
```

To set base url using environment variables use

```
export WEBTAU_URL=http://another-server
```

# Proxy {style: "api"}

Use `httpProxy` to set proxy for the HTTP calls performed through `http.get`, `http.post`, etc 

`````tabs
Groovy:

:include-file: scenarios/rest/proxy/webtau.proxy.cfg.groovy {
    title: "webtau.cfg.groovy"
}

Java:

```java {title: "src/test/resources/webtau.properties"}
httpProxy = my_proxy_server_to_use.com:3873
```
`````