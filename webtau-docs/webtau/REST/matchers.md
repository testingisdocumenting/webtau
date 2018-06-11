---
type: two-sides
---

# Equality

Identifiers inside validation closure are automatically mapped to a response body. So typing 

```groovy
id.shouldNot == null 
``` 

will be automatically mapped to response `body.id`. If you need to access body explicitly use 

```groovy
body.shouldNot == [1, 2, 3] 
``` 

:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {entry: "equality matcher", bodyOnly: true}

:include-meta: {rightSide: true}
:include-json: objectTestResponse.json {title: "response", pathsFile: "doc-artifacts/end-point-object-equality-matchers/paths.json"}

# Greater/Less/Equal

:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {entry: "compare numbers with greater less matchers", bodyOnly: true}

:include-meta: {rightSide: true}
:include-json: numbersTestResponse.json {title: "response", pathsFile: "doc-artifacts/end-point-numbers-matchers/paths.json"}

# Contain

:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {entry: "contain matcher", bodyOnly: true}

:include-meta: {rightSide: true}
:include-json: listTestResponse.json {title: "response", pathsFile: "doc-artifacts/end-point-list-contain-matchers/paths.json"}
