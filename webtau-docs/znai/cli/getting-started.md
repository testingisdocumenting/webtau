Webtau provides `cli.` module to run and validate command line tools.
Combine it with other modules to create powerful tests: use `cli` to run a tool, and 
`http`, `graphql`, `browser`, or `db` to validate the outcome.

# Installation

If you use Webtau Groovy runner, the `cli.` module is available as part of convenient static import:

:include-file: scenarios/cli/basicLs.groovy {
  title: "convenient static import",
  includeRegexp: "WebTauGroovyDsl"
} 

Note: Import is optional and is added for IDEs benefits.

# Groovy Runner Example 

:include-file: scenarios/cli/basicLs.groovy {
  title: "basicLs.groovy",
  excludeRegexp: ["package", "import"]
} 

:include-cli-command: webtau basicLs.groovy
