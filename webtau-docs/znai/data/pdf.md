# Read File Or Resource

:include-java-doc: org/testingisdocumenting/webtau/data/DataPdf.java {entry: "read(String)"}

:include-groovy: org/testingisdocumenting/webtau/data/DataPdfTest.groovy {
  title: "read and assert pdf",
  entry: "read pdf from resource",
  bodyOnly: true
} 

:include-markdown: import-ref.md

# HTTP response

Use `data.pdf.parse(body)` to parse and assert PDF content from binary response.

```tabs
Groovy:
:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {
  title: "HTTP pdf response assertion",
  entry: "download pdf and assert page text using contains", 
  bodyOnly: true
}

Java:
:include-java: org/testingisdocumenting/webtau/http/HttpJavaTest.java {
  title: "HTTP pdf response assertion",
  entry: "downloadPdfAndAssertPageTextUsingContains", 
  bodyOnly: true
}
```

Learn More about [HTTP response](HTTP/body#pdf-response)

# Read Bytes

:include-groovy: org/testingisdocumenting/webtau/data/DataPdfTest.groovy {
  title: "read and assert pdf from bytes",
  entry: "read pdf from bytes",
  bodyOnly: true
} 


