# Standard Header

Standard headers like `Content-Type` and `Accept` are set on your behalf. 
When payload content is present then values are based on the content type you are sending. 
When no payload is present, it defaults to `application/json`.   

# Common Header

If each http request requires the same header you can specify that header using `httpHeaderProvider`. 
Common example is specifying authentication header.
 
:include-file: examples/scenarios/rest/headers/webtau.cfg {title: "webtau.cfg"}

Where `Auth.&authHeader` is implemented as follows:

:include-file: examples/scenarios/rest/headers/auth/Auth.groovy {title: "scenarios/rest/headers/auth/Auth.groovy"}

This removes implementation details from your tests and makes them less brittle.  

# Explicit Header

To explicitly set header pass `http.header(values)` as an additional parameter.

:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {entry: "explicit header passing example", bodyOnly: true}

Additionally `http.header` accepts values as a map.

:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {entry: "header creation", bodyOnly: true}

# Mime Type Combined With Payload

Use `http.body` to combine `Content-Type` and payload.

:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {title: "Combined type and payload", entry: "explicit binary mime types combined with request body", bodyOnly: true}

If you need a standard type consider using  

:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {title: "Binary content shortuct", entry: "implicit binary mime types combined with request body", bodyOnly: true}

:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {title: "Text content shortcut", entry: "implicit text mime types combined with request body", bodyOnly: true}
  
# Response Header

To validate values from response header use `header` object.

:include-groovy: com/twosigma/webtau/http/HttpTest.groovy {entry: "header assertion with shortcut", bodyOnly: true}

At the moment only `location`, `contentLocation`, `contentLength` have camelCase shortcuts.
All the other header values you need to use `['Header-Name']` syntax.