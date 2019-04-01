Webtau offers a number of ways of specifying query parameters.

# Within the URL

:include-groovy: com/twosigma/webtau/http/HttpGroovyTest.groovy {entry: "query params in url", bodyOnly: true}

# Map based `query` helper

This version of the `http.query` helper is generally more suitable for Groovy.

:include-groovy: com/twosigma/webtau/http/HttpGroovyTest.groovy {entry: "build query params using map helper", bodyOnly: true}

# Vararg based `query` helper

This version of the `http.query` helper is generally more suitable for Java.

:include-groovy: com/twosigma/webtau/http/HttpGroovyTest.groovy {entry: "build query params using var arg helper", bodyOnly: true}
