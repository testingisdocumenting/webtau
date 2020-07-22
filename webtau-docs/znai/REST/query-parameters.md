# Passing Query Parameters 

Webtau offers a number of ways of specifying query parameters:

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
    title: "embedding query params directly in url",
    entry: "query params in url example",
    bodyOnly: true
}

Use `Map` as a second parameter to pass query parameters. Suitable for languages that support in-line creation of `Map`.


:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
    title: "pass query parameters as Map",
    entry: "query params using query as map example",
    bodyOnly: true
}

Only `http.get` has a `Map` variant, for `http.put`, `http.post`, etc you must use `http.query`. 

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
    title: "pass query parameters using http.query",
    entry: "query params using query method example",
    bodyOnly: true
}

Additionally `http.query` has a vararg variant which is more convenient for `Java`. 

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
    title: "http.query vararg variant",
    entry: "query params using query method and comma example",
    bodyOnly: true
}

# Parameters Encoding

All query parameters are encoded automatically. 

:include-file: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
    title: "query parameter with url forbidden characters",
    entry: "query params encoding",
    startLine: "query params encoding snippet start",
    endLine: "query params encoding snippet end",
    excludeStartEnd: true,
    excludeRegexp: "http.doc"
}

:include-file: doc-artifacts/query-params-encoding/request.url.txt {title: "automatic encoding"}

