# Overloaded Calls

`http.get|post|put|delete` methods have overloads to let you supply additional data:
* Query parameters
* Request header
* Payload (*where applicable*) 
* Validation block

Overloads maintain relative order, but you can omit any data.

# GET

:include-java: com/twosigma/webtau/http/Http.java {entry: "get(String, HttpQueryParams, HttpHeader, HttpResponseValidatorWithReturn)", signatureOnly: true}

```tabs 
Groovy: 
:include-groovy: com/twosigma/webtau/http/HttpGroovyOverloadsTest.groovy {entry: "get full return syntax example", bodyOnly: true, title: "full signature with query, header and validation block", commentsType: "inline"}
:include-groovy: com/twosigma/webtau/http/HttpGroovyOverloadsTest.groovy {entry: "get no header return syntax example", bodyOnly: true, title: "without header"}
:include-groovy: com/twosigma/webtau/http/HttpGroovyOverloadsTest.groovy {entry: "get no query return syntax example", bodyOnly: true, title: "without query"}
:include-groovy: com/twosigma/webtau/http/HttpGroovyOverloadsTest.groovy {entry: "get only validation return syntax example", bodyOnly: true, title: "only validation"}
:include-groovy: com/twosigma/webtau/http/HttpGroovyOverloadsTest.groovy {entry: "get only syntax example", bodyOnly: true, title: "only call"}

Java: 

:include-java: com/twosigma/webtau/http/HttpJavaOverloadsTest.java {entry: "getFullReturnSyntaxExample", bodyOnly: true, title: "full signature with query, header and validation block", commentsType: "inline"}
:include-java: com/twosigma/webtau/http/HttpJavaOverloadsTest.java {entry: "getNoHeaderReturnSyntaxExample", bodyOnly: true, title: "without header"}
:include-java: com/twosigma/webtau/http/HttpJavaOverloadsTest.java {entry: "getNoQueryReturnSyntaxExample", bodyOnly: true, title: "without query"}
:include-java: com/twosigma/webtau/http/HttpJavaOverloadsTest.java {entry: "getOnlyValidationReturnSyntaxExample", bodyOnly: true, title: "only validation"}
:include-java: com/twosigma/webtau/http/HttpJavaOverloadsTest.java {entry: "getOnlySyntaxExample", bodyOnly: true, title: "only call"}
``` 

:include-groovy: com/twosigma/webtau/http/HttpGroovyOverloadsTest.groovy {entry: "get full return syntax example", bodyOnly: true, title: "full signature with query, header and validation block", commentsType: "inline"}

:include-java: com/twosigma/webtau/http/HttpJavaOverloadsTest.java {entry: "getFullReturnSyntaxExample", bodyOnly: true, title: "with return value", commentsType: "inline"}



