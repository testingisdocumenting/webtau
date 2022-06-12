# JSON Request

Methods `http.post`, `http.put`, `http.delete` automatically converts `java.util.Map` or `java.util.List` into 
`application/json` request 

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyOverloadsTest.groovy {
  title: "implicit application/json",
  entry: "post body only syntax example",
  bodyOnly: true
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaOverloadsTest.java {
  title: "implicit application/json",
  entry: "postBodyOnlySyntaxExample", 
  bodyOnly: true
}

 Note: For Java example uses a `http.json` shortcut but Map/List also works. 
```

# Generic Request

Use `http.body` to create generic body request.

```tabs
Groovy: :include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {title: "combined type and payload", entry: "explicit binary mime types combined with request body", bodyOnly: true}
Java: :include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {title: "combined type and payload", entry: "explicitBinaryMimeTypesCombinedWithRequestBody", bodyOnly: true}
```


# Standard Shortcuts

WebTau provides shortcuts for Standard MIME types

```tabs
Groovy:
 
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  title: "json content as vararg", 
  entry: "shortcut json mime types combined with request body", 
  bodyOnly: true
}
 
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  title: "json content as text", 
  entry: "shortcut json text mime types combined with request body", 
  bodyOnly: true
}
 
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  title: "binary content shortuct", 
  entry: "shortcut binary mime types combined with request body", 
  bodyOnly: true
}

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  title: "text content shortcut", 
  entry: "shortcut text mime types combined with request body", 
  bodyOnly: true
}

Java: 

:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  title: "json content as vararg", 
  entry: "shortcutJsonMimeTypesCombinedWithRequestBody", 
  bodyOnly: true
}

:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  title: "json content as text", 
  entry: "shortcutJsonTextMimeTypesCombinedWithRequestBody", 
  bodyOnly: true
}

:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  title: "binary content shortuct", 
  entry: "shortcutBinaryMimeTypesCombinedWithRequestBody", 
  bodyOnly: true
}

:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  title: "text content shortcut", 
  entry: "shortcutTextMimeTypesCombinedWithRequestBody", 
  bodyOnly: true
}
```

Note: `:identifier: http.application.json {validationPath: "org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy"}` 
is long a form of `:identifier: http.json {validationPath: "org/testingisdocumenting/webtau/http/HttpJavaTest.java"}` 
and is there for completeness purpose. 
\
\
There is no behavior difference between passing an instance of `java.util.Map` and `http.json`


# Parsed Response 

Special [Data Node](HTTP/data-node) `body` represents parsed response. Use it to validate response values.

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyOverloadsTest.groovy {
  title: "use body for validation",
  entry: "get only validation with body syntax example",
  bodyOnly: true
}

 Note: For Groovy `body` is optional and when not specified, will be used implicitly  

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyOverloadsTest.groovy {
  title: "optional body",
  entry: "get only validation syntax example",
  bodyOnly: true
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaOverloadsTest.java {
  title: "use body for validation",
  entry: "getOnlyValidationSyntaxExample", 
  bodyOnly: true
}
```

# Raw Response

Use `:identifier: body.getTextContent() {validationPath: "org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy"}` to access original text content

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  title: "access to raw text content",
  entry: "access to raw text content",
  surroundedBy: "doc-snippet",
  bodyOnly: true
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  title: "access to raw text content",
  entry: "accessToRawTextContent", 
  surroundedBy: "doc-snippet",
  bodyOnly: true
}
```
