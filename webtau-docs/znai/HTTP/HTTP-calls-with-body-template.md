```java {title: "full signature"}
http.${method}(url, [queryParams], [header], [body], [validationBlock])
```

```api-parameters
url, String, [relative](getting-started/configuration#config-file) or absolute URL to send `GET` request to
queryParams, HttpQueryParams | Map, [query parameters](HTTP/query-parameters) to attach to URL 
header, HttpHeader, [request header](HTTP/header) to send 
body, HttpRequestBody | Map | List, request body to send with a request 
validationBlock, HttpResponseValidator, validation block of code to [assert](HTTP/matchers) and access response body and header
```

:include-json: doc-artifacts/${responseArtifact}/response.json {title: "${responseTitle}", pathsFile: "doc-artifacts/${responseArtifact}/paths.json"}

```tabs 
Groovy: 
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyOverloadsTest.groovy {
  title: "full signature with query, header, body and validation block",
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
  title: "body only",
  entry: "${method} body only syntax example", 
  bodyOnly: true
}

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyOverloadsTest.groovy {
  title: "validation only",
  entry: "${method} validation only syntax example", 
  bodyOnly: true
}

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyOverloadsTest.groovy {
  title: "no validation",
  entry: "${method} only syntax example", 
  bodyOnly: true
}

Java: 
:include-java: org/testingisdocumenting/webtau/http/HttpJavaOverloadsTest.java {
  title: "full signature with query, header, body and validation block", 
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
  title: "body only", 
  entry: "${method}BodyOnlySyntaxExample", 
  bodyOnly: true
}

:include-java: org/testingisdocumenting/webtau/http/HttpJavaOverloadsTest.java {
  title: "validation only", 
  entry: "${method}ValidationOnlySyntaxExample", 
  bodyOnly: true
}

:include-java: org/testingisdocumenting/webtau/http/HttpJavaOverloadsTest.java {
  title: "no validation", 
  entry: "${method}NoValidationSyntaxExample", 
  bodyOnly: true
}
```
