Here we cover essential configuration for HTTP testing.

To learn basics about configuration, head over to [Configuration Getting Started](getting-started/configuration).
\
To learn about all configuration capabilities, head over to [Configuration page](configuration/groovy-config-file). 

# Base URL {style: "api"}

:include-markdown: configuration/common/base-url.md

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