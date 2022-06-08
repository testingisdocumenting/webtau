WebTau provides `cli.` module to run and validate command line tools.
Combine it with other modules to create powerful tests: use `cli` to run a tool, and 
`http`, `graphql`, `browser`, or `db` to validate the outcome.

:include-file: scenarios/cli/basicLs.groovy {
  title: "basicLs.groovy",
  excludeRegexp: ["package", "import", "doc.capture"]
} 

:include-cli-command: webtau basicLs.groovy

:include-file: doc-artifacts/ls-run/out.txt {highlightPath: "doc-artifacts/ls-run/out.matched.txt"}