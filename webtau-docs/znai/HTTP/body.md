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
