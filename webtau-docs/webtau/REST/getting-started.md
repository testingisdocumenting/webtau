:include-markdown: common/installation.md

# Bare Minimum

:include-file: examples/scenarios/rest/simpleGet.groovy {title: "examples/scenarios/rest/simpleGet.groovy"}

To run test, navigate to `examples` dir and

:include-cli-command: webtau scenarios/rest/simpleGet.groovy --url=https://my-server {paramsToHighlight: "url"}

:include-markdown: common/note-package-import.md

# Config File

Url parameter can be moved to a `webtau.cfg` file.

:include-file: examples/scenarios/rest/urlOnly.cfg {title: "examples/scenarios/rest/webtau.cfg"}

[Specify multiple environments](configuration/environments) to streamline test execution.
