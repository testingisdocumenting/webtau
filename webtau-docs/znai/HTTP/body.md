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

# JSON Request From File

[Data JSON](utilities/data#json-read) module has convenient methods to read JSON from resource/file as list/map/object

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyOverloadsTest.groovy {
  title: "implicit application/json",
  entry: "post body from file syntax example",
  bodyOnly: true
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaOverloadsTest.java {
  title: "implicit application/json",
  entry: "postBodyFromFileSyntaxExample", 
  bodyOnly: true
}
```

# Generic Request

Use `http.body` to create generic body request.

```tabs
Groovy: :include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {title: "combined type and payload", entry: "explicit binary mime types combined with request body", bodyOnly: true}
Java: :include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {title: "combined type and payload", entry: "explicitBinaryMimeTypesCombinedWithRequestBody", bodyOnly: true}
```


# Content-Type Shortcuts

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
is a long form of `:identifier: http.json {validationPath: "org/testingisdocumenting/webtau/http/HttpJavaTest.java"}` 
and is there for completeness purpose. 
\
\
There is no behavior difference between passing an instance of `java.util.Map` and `http.json`

# Form File Data

Consider example where backend expects a file as `multipart/form-data`.
Field `file` defines content.  
Backend responds with file name and file description it received.

To send `multipart/form-data`, use `http.formData` to build a request body. 

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  title: "file upload",
  entry: "file upload example simple",
  bodyOnly: true
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  title: "file upload",
  entry: "fileUploadExampleSimple", 
  bodyOnly: true
}
```

Use `http.formFile` to override file name.

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  title: "file name override",
  entry: "file upload example with file name override", 
  bodyOnly: true
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  title: "file upload",
  entry: "fileUploadExampleWithFileNameOverride", 
  bodyOnly: true
}
```

Multiple form fields can be specified:

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  title: "multiple form fields",
  entry: "file upload example multiple fields", 
  bodyOnly: true
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  title: "file upload",
  entry: "fileUploadExampleMultipleFields", 
  bodyOnly: true
}
```

To pass a file content directly, use

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  title: "file upload in-memory content",
  entry: "file upload example with in-memory content", 
  bodyOnly: true
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  title: "file upload in-memory content",
  entry: "fileUploadExampleWithInMemoryContent", 
  bodyOnly: true
}
```

Note: no file name is passed and this particular backend generated file name on your behalf.

Use `http.formFile` to provide a file name

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  title: "providing name",
  entry: "file upload example with in-memory content and file name",
  bodyOnly: true
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  title: "file upload in-memory content",
  entry: "fileUploadExampleWithInMemoryContentAndFileName", 
  bodyOnly: true
}
```

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
