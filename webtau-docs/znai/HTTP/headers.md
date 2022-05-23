# Standard Header

Standard headers like `Content-Type` and `Accept` are set on your behalf. 
When payload content is present then values are based on the content type you are sending. 
When no payload is present, it defaults to `application/json`.   

# Implicit Header

If each http request requires the same header you can specify that header using `httpHeaderProvider`. 
Common example is specifying authentication header.
 
:include-file: scenarios/rest/headers/webtau.cfg.groovy {title: "webtau.cfg.groovy"}

Where `Auth.&authHeader` is implemented as follows:

:include-file: scenarios/rest/headers/auth/Auth.groovy {title: "scenarios/rest/headers/auth/Auth.groovy"}

Implicit headers help to clean up tests from implementation details

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

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {title: "Combined type and payload", entry: "explicit binary mime types combined with request body", bodyOnly: true}

If you need a standard type consider using  

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {title: "Binary content shortuct", entry: "post implicit binary mime types combined with request body", bodyOnly: true}

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {title: "Text content shortcut", entry: "implicit text mime types combined with request body", bodyOnly: true}
  
# Response Header

To validate values from response header use `header` object.

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "header assertion with shortcut", bodyOnly: true}

At the moment only `location`, `contentLocation`, `contentLength` have camelCase shortcuts.
All the other header values you need to use `['Header-Name']` syntax.