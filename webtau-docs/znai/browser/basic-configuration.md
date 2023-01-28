# Base URL

Robust tests don't specify the full URL of an application under test.
Instead you only pass a relative URL to functions like `open`.

:include-groovy: scenarios/ui/basic.groovy

Define base URL either inside a `webtau.cfg.groovy` file

:include-file: scenarios/ui/webtau.cfg.groovy

or pass as a command line argument `--url=http://...`

# Browser Only Base URL

When you use `url` parameter you set base url for both `REST` and `UI` testing. 

Use `browserUrl` to specify `UI` only base url. This can be handy when your backend and frontend live separately.  

# Browser Id

Use `browserId` via command line, or a config file parameter to specify which browser to use.

:include-cli-command: webtau --browserId=firefox

Note: Default `browserId` is `chrome`

# Access To Base Url

To access base url or port use:

:include-file: org/testingisdocumenting/webtau/browser/BrowserConfigTest.groovy {surroundedBy: ["base-url", "base-port"]}