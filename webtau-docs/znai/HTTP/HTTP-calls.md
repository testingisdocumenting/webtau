# Overloaded Calls

`http.get|post|put|delete` methods have overloads to let you supply additional data in addition to URL:
* Query parameters
* Request header
* Payload (*where applicable*)
* Validation block

Overloads maintain relative order, but you can omit any additional data.

Here is `http.get` quick example:

```tabs 
Groovy: 
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyOverloadsTest.groovy {
  title: "GET request with passed header",
  entry: "get no query syntax example",
  bodyOnly: true
}
:include-markdown: import-ref.md

Java: 
:include-java: org/testingisdocumenting/webtau/http/HttpJavaOverloadsTest.java {
  title: "GET request with passed header",
  entry: "getNoQuerySyntaxExample", 
  bodyOnly: true
}
:include-markdown: import-ref.md
``` 

:include-json: queryTestResponse.json {title: "GET /query response", pathsFile: "doc-artifacts/get-full-syntax-assertion/paths.json"}

# Implicit statusCode Check

By default, WebTau automatically assert `statusCode` based on method.

| Method              | Implicitly Expected Code |
| --------------------|--------------------------|
| GET                 | 200                      |
| POST                | 201                      |
| PUT                 | 200                      |
| PUT (no response)   | 204                      |
| DELETE              | 200                      |
| DELETE (no response)| 204                      |

To turn it off, provide an explicit `statusCode` assertion.

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyOverloadsTest.groovy {
    title: "explicit statusCode check",
    entry: "post statusCode syntax example", 
    bodyOnly: true,
    commentsType: "inline"
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaOverloadsTest.java {
  title: "explicit statusCode check",
  entry: "postStatusCodeSyntaxExample", 
  bodyOnly: true,
  commentsType: "inline"
}
```

# GET {style: "api"}

```template HTTP-calls-no-body-template.md
method:get
responseArtifact:get-full-syntax-assertion
responseTitle:GET /query response
```

# POST {style: "api"}

```template HTTP-calls-with-body-template.md
method:post
responseArtifact:post-full-syntax-assertion
responseTitle:POST /chat response
```

# PUT {style: "api"}

```template HTTP-calls-with-body-template.md
method:put
responseArtifact:post-full-syntax-assertion
responseTitle:PUT /chat/id1 response
```

# DELETE {style: "api"}

```template HTTP-calls-no-body-template.md
method:delete
responseArtifact:post-full-syntax-assertion
responseTitle:DELETE /chat/id1 response
```

# PATCH {style: "api"}

```template HTTP-calls-with-body-template.md
method:patch
responseArtifact:post-full-syntax-assertion
responseTitle:PATCH /chat/id1 response
```

# PING {style: "api"}

Use `ping` to check if an end point responds `200` to `GET` method

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  title: "if else ping example",
  entry: "ping if else example", 
  bodyOnly: true
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  title: "if else ping example",
  entry: "pingIfElseExample", 
  bodyOnly: true
}
```

```java {title: "full signature"}
boolean http.ping(url, [queryParams], [header]) 
```

```api-parameters
url, String, [relative](getting-started/configuration#config-file) or absolute URL to send `GET` request to
queryParams, HttpQueryParams | Map, [query parameters](HTTP/query-parameters) to attach to URL 
header, HttpHeader, [request header](HTTP/header) to send 
```

```tabs 
Groovy: 
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyOverloadsTest.groovy {
  title: "full signature with query and header",
  entry: "ping all params", 
  bodyOnly: true,
  commentsType: "inline"
}

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyOverloadsTest.groovy {
  title: "without header",
  entry: "ping no header",
  bodyOnly: true 
}

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyOverloadsTest.groovy {
  title: "without query",
  entry: "ping no query",
  bodyOnly: true
}

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyOverloadsTest.groovy {
  title: "url only",
  entry: "ping url only",
  bodyOnly: true
}

Java: 
:include-java: org/testingisdocumenting/webtau/http/HttpJavaOverloadsTest.java {
  title: "full signature with query and header", 
  entry: "pingAllParams", 
  bodyOnly: true,
  commentsType: "inline"
}

:include-java: org/testingisdocumenting/webtau/http/HttpJavaOverloadsTest.java {
  title: "without header",
  entry: "pingNoHeader", 
  bodyOnly: true
}

:include-java: org/testingisdocumenting/webtau/http/HttpJavaOverloadsTest.java {
  title: "without query",
  entry: "pingNoQuery", 
  bodyOnly: true
}

:include-java: org/testingisdocumenting/webtau/http/HttpJavaOverloadsTest.java {
  title: "url only",
  entry: "pingUrlOnly", 
  bodyOnly: true
}
``` 