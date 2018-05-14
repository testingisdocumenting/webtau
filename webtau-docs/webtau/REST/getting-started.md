# Installation

Download and unzip [webtau](https://github.com/twosigma/webtau/releases/). Add it to your `PATH`.

# Bare Minimum

:include-file: examples/rest/restGet.groovy {title: "examples/rest/restGet.groovy"}

To run test, navigate to `examples` dir and

:include-cli-command: webtau rest/restGet.groovy --url=https://my-server {paramsToHighlight: "url"}

Note: using `package` and `import` is optional and is mainly for IDE auto completion. Imports will be added implicitly
during command line run.  

# Config File

Url parameter can be moved to a `webtau.cfg` file.

:include-file: examples/rest/url.cfg {title: "examples/rest/webtau.cfg"}

[Specify multiple environments](configuration/environments) to streamline test execution.
