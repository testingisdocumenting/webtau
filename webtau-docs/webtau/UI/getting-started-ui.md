:include-markdown: {firstAvailable: ["installation-company-specific-groovy-runner.md", "installation-groovy-runner.md"]}

# Bare Minimum

:include-file: examples/scenarios/ui/basic.groovy {title: "examples/scenarios/ui/basic.groovy"}

To run test, navigate to `examples` dir and

:include-cli-command: webtau scenarios/ui/basic.groovy --url=https://my-server {paramsToHighlight: "url"}

:include-markdown: common/note-package-import.md

# Config File

Url parameter can be moved to a `webtau.cfg` file.

:include-file: examples/scenarios/ui/webtau.cfg {title: "examples/scenarios/ui/webtau.cfg"}

[Specify multiple environments](configuration/environments) to streamline test execution.
