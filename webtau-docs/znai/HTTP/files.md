# Upload File 

Consider example where backend expects a file as `multipart/form-data`. 
Field `file` defines content.  
Backend responds with file name and file description it received.  

To `POST` form data, use the `http.post`. 
Pass `http.formData` as second parameter. In previous examples, second parameter was `JSON` payload.

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "file upload example simple", bodyOnly: true}

Use `http.formFile` to override file name.

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "file upload example with file name override", bodyOnly: true}

Multiple form fields can be specified: 

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "file upload example multiple fields", bodyOnly: true}

# Upload In-Memory Content

To pass file content from memory use

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "file upload example with in-memory content", bodyOnly: true}

Note: no file name is passed and this particular backend generated file name on your behalf.

Use `http.formFile` to provide a file name

:include-groovy: org/testingisdocumenting/webtau/http/HttpGroovyTest.groovy {entry: "file upload example with in-memory content and file name", bodyOnly: true}

# Download PDF

Assert PDF content using `pdf(body)` function.

:include-groovy: org/testingisdocumenting/webtau/pdf/PdfHttpTest.groovy {entry: "download pdf and assert page text using contains", bodyOnly: true}

If neem more than one assertion, assign `pdf` result to a local variable.

:include-groovy: org/testingisdocumenting/webtau/pdf/PdfHttpTest.groovy {entry: "download pdf and assert page text using equal and contains", bodyOnly: true}

Note: Use pdf assertions for sanity checks: presence of correct client names or account numbers. Implement comprehensive PDF generation logic tests in unit tests.