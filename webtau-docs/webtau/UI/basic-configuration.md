# Base URL

Robust tests don't specify the full URL of an application under test.
Instead you only pass a relative URL to functions like `open`.

:include-groovy: scenarios/ui/basic.groovy

Define base URL portion either inside a `webtau.cfg` file

:include-file: scenarios/ui/webtau.cfg {lang: "groovy"}

or pass as a command line argument `--url=http://...`

