# Passing Query Parameters 

WebTau offers a number of ways of specifying query parameters:

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
    title: "embedding query params directly in url",
    entry: "query params in url example",
    bodyOnly: true
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
    title: "embedding query params directly in url",
    entry: "queryParamsInUrlExample",
    bodyOnly: true
}
```

Use `Map` as a second parameter to pass query parameters. Suitable for languages that support in-line creation of `Map`.

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
    title: "pass query parameters as Map",
    entry: "query params using query as map example",
    bodyOnly: true
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
    title: "pass query parameters as Map",
    entry: "queryParamsUsingQueryAsMapExample",
    bodyOnly: true
}
```

Only `http.get` has a `Map` variant, for `http.put`, `http.post`, etc you need to pass `http.query`. 

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
    title: "pass query parameters using http.query",
    entry: "query params using query method example",
    bodyOnly: true
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
    title: "pass query parameters as Map",
    entry: "queryParamsUsingQueryMethodExample",
    bodyOnly: true
}
```

# Parameters Encoding

All query parameters are encoded automatically. 

```tabs
Groovy:
:include-file: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
    title: "query parameter with url incompatible characters",
    surroundedBy: "query-params-encoding"
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
    title: "query parameter with url incompatible characters",
    entry: "queryParamsEncoding",
    bodyOnly: true
}
```

:include-file: doc-artifacts/query-params-encoding/request.url.txt {title: "automatic encoding"}

