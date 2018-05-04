# Request Header Provider

Specify `httpHeaderProvider` config parameter to add additional header information to all your requests

:include-file: examples/rest/headers/test.cfg

Where `Auth.&authHeader` implemented as

:include-file: examples/rest/headers/auth/Auth.groovy

This removes implementation details from your tests and make them less brittle.  