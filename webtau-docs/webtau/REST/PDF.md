# Asserting Text

If response contains a pdf file you can assert its content using `pdf(body)` function.

:include-groovy: com/twosigma/webtau/pdf/PdfHttpTest.groovy {entry: "download pdf and assert page text using contains", bodyOnly: true}
 
If more than one assertion needs to be made, assign `pdf` result to a local variable.
 
:include-groovy: com/twosigma/webtau/pdf/PdfHttpTest.groovy {entry: "download pdf and assert page text using equal and contains", bodyOnly: true}

Note: use pdf assertions for sanity checks like presence of correct client names or account numbers. Leave comprehensive pdf generation test to unit tests.