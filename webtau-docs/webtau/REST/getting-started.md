# Bare Minimum

:include-file: examples/rest/restGet.groovy {title: "examples/rest/restGet.groovy"}

To run test, navigate to `examples` dir and

:include-cli-command: webtau rest/restGet.groovy --url=https://my-server {paramsToHighlight: "url"}

# Config File

Url parameter can be moved to a `test.cfg` file.

:include-file: examples/rest/url.cfg {title: "examples/rest/test.cfg"}

[Specify multiple environments](configuration/environments) to streamline tests execution.
