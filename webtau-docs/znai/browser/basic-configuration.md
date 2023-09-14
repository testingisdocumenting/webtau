# Base URL

:include-markdown: configuration/common/base-url.md

# Browser Only Base URL

When you use `url` parameter you set base url for both `REST` and `UI` testing. 

Use `browserUrl` to specify `UI` only base url. This can be handy when your backend and frontend live separately.  

# Browser Id

Use `browserId` via command line, system property, env variable or config file parameter to specify which browser to use.

`````tabs
Groovy:
:include-cli-command: webtau --browserId=firefox

```cli
export WEBTAU_BROWSER_ID=firefox
```
Java:
```cli
... -DbrowserId=firefox
```
```cli
export WEBTAU_BROWSER_ID=firefox
```
`````
Note: Default `browserId` is `chrome`

# Access To Base Url

To access base url or port use:

:include-file: org/testingisdocumenting/webtau/browser/BrowserConfigTest.groovy {surroundedBy: ["base-url", "base-port"]}