# Request Header Provider

Specify `httpHeaderProvider` config parameter to add additional header information to all your requests:

:include-file: examples/rest/headers/webtau.cfg

Where `Auth.&authHeader` is implemented as follows:

:include-file: examples/rest/headers/auth/Auth.groovy

This removes implementation details from your tests and makes them less brittle.  
