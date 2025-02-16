```tabs
Groovy:

:include-file: scenarios/rest/urlOnly.cfg.groovy {
  title: "webtau.cfg.groovy",
  include: "localhost"
}

 Note: WebTau treats groovy config file as code

Alternatively pass `url` as a command line argument `--url=http://...`

Java:

:include-file: webtau-junit5-examples/src/test/resources/webtau.properties {
  title: "src/test/resources/webtau.properties",
  include: "localhost"
}

When you use JUnit like runners, e.g. [JUnit5](getting-started/installation#junit5), WebTau expects file named
`webtau.properties` to be present in test classpath, e.g. test resources:

Alternatively pass `url` as a system property `-Durl=http://...`
```

To set base url using environment variables use

```
export WEBTAU_URL=http://another-server
```
