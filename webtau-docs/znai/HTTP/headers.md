# Standard Header

Standard headers like `Content-Type` and `Accept` are set on your behalf. 
When payload content is present then values are based on the content type you are sending. 
When no payload is present, it defaults to `application/json`.   

# Implicit Header

Webtau has a way to provide headers for each call implicitly. 
Use it to provide things like authentication, version, etc. header values.

Implicit headers goal is to reduce expose to implementation details and make tests more robust.

```tabs
Groovy:

:include-file: scenarios/rest/headers/webtau.cfg.groovy {
  title: "webtau.cfg.groovy",
  highlight: "authHeader"
}

Where `Auth.&authHeader` is implemented as follows:

:include-file: scenarios/rest/headers/auth/Auth.groovy {title: "scenarios/rest/headers/auth/Auth.groovy"}

Java:
In case of JUnit like runners, webtau uses [Service Loaders](https://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html) 
to locate header providers

:include-file: src/test/resources/META-INF/services/org.testingisdocumenting.webtau.http.config.WebTauHttpConfiguration {
  autoTitle: true
}

:include-file: src/test/java/com/example/tests/junit5/config/HttpAuthHeaderProvider.java {
  autoTitle: true
}
```

Note: Read [Persona Auth](persona/HTTP-persona) to learn about ways to streamline authentication 

# Explicit Header

To explicitly set header pass `http.header(values)` as an additional parameter.

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  entry: "explicit header passing example", 
  bodyOnly: true}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  entry: "explicitHeaderPassingExample",
  bodyOnly: true}
```

Additionally `http.header` accepts values as a map.

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  entry: "header creation",
  excludeRegexp: "doc-exclude",
  bodyOnly: true}
  
Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  entry: "headerCreation",
  excludeRegexp: "doc-exclude",
  bodyOnly: true}
```

Use `.with` to create a new instance of a header based on the existing one plus additional values

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  entry: "header with",
  surroundedBy: "// example",
  bodyOnly: true}
  
Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  entry: "headerWith",
  surroundedBy: "// example",
  bodyOnly: true}
```


# Mime Type Combined With Payload

Use `http.body` to combine `Content-Type` and payload.

```tabs
Groovy: :include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {title: "combined type and payload", entry: "explicit binary mime types combined with request body", bodyOnly: true}
Java: :include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {title: "combined type and payload", entry: "explicitBinaryMimeTypesCombinedWithRequestBody", bodyOnly: true}
```

Standard types have shortcuts  

```tabs
Groovy:
 
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {title: "binary content shortuct", entry: "post implicit binary mime types combined with request body", bodyOnly: true}
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {title: "text content shortcut", entry: "implicit text mime types combined with request body", bodyOnly: true}

Java: 

:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {title: "combined type and payload", entry: "postImplicitBinaryMimeTypesCombinedWithRequestBody", bodyOnly: true}
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {title: "combined type and payload", entry: "postImplicitTextMimeTypesCombinedWithRequestBody", bodyOnly: true}
```

  
# Response Header

To validate values from response header use `header` object.

```tabs

Groovy: :include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {title: "header assertion with shortcut", entry: "header assertion with shortcut", bodyOnly: true}
Java: :include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {title: "header assertion with shortcut", entry: "headerAssertionWithShortcut", bodyOnly: true}

```

At the moment only `location`, `contentLocation`, `contentLength` have camelCase shortcuts.
All the other header values you need to use `['Header-Name']` syntax.