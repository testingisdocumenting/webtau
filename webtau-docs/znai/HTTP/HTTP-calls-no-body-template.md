```java {title: "full signature"}
http.${method}(url, [queryParams], [header], [validationBlock])
```

```api-parameters
url, String, [relative](getting-started/configuration#config-file) or absolute URL to send `GET` request to
queryParams, HttpQueryParams | Map, [query parameters](HTTP/query-parameters) to attach to URL 
header, HttpHeader, [request header](HTTP/header) to send 
validationBlock, HttpResponseValidator, validation block of code to [assert](HTTP/matchers) and access response body and header
```

:include-json: doc-artifacts/${responseArtifact}/response.json {title: "${responseTitle}", pathsFile: "doc-artifacts/${responseArtifact}/paths.json"}

```tabs 
Groovy: 
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyOverloadsTest.groovy {
  title: "full signature with query, header and validation block",
  entry: "${method} full return syntax example", 
  bodyOnly: true,
  commentsType: "inline"
}

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyOverloadsTest.groovy {
  title: "without header",
  entry: "${method} no header syntax example",
  bodyOnly: true 
}

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyOverloadsTest.groovy {
  title: "without query",
  entry: "${method} no query syntax example",
  bodyOnly: true
}

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyOverloadsTest.groovy {
  title: "only validation",
  entry: "${method} only validation syntax example",
  bodyOnly: true
}

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyOverloadsTest.groovy {
  title: "no validation",
  entry: "${method} only syntax example", 
  bodyOnly: true
}

Java: 
:include-java: org/testingisdocumenting/webtau/http/HttpJavaOverloadsTest.java {
  title: "full signature with query, header and validation block", 
  entry: "${method}FullReturnSyntaxExample", 
  bodyOnly: true,
  commentsType: "inline"
}

:include-java: org/testingisdocumenting/webtau/http/HttpJavaOverloadsTest.java {
  title: "without header",
  entry: "${method}NoHeaderSyntaxExample", 
  bodyOnly: true
}

:include-java: org/testingisdocumenting/webtau/http/HttpJavaOverloadsTest.java {
  title: "without query",
  entry: "${method}NoQuerySyntaxExample", 
  bodyOnly: true
}

:include-java: org/testingisdocumenting/webtau/http/HttpJavaOverloadsTest.java {
  title: "only validation",
  entry: "${method}OnlyValidationSyntaxExample", 
  bodyOnly: true
}

:include-java: org/testingisdocumenting/webtau/http/HttpJavaOverloadsTest.java {
  title: "no validation",
  entry: "${method}OnlySyntaxExample", 
  bodyOnly: true
}
``` 