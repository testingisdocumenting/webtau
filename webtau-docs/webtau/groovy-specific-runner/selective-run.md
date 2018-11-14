# sscenario

Define tests with `sscenario` to only run those tests and skip all the other tests defined in all the test files.
Useful during tests creation or debugging.

:include-file: scenarios/concept/runOnlySelected.groovy {title: "Selective tests run", highlight: "sscenario"}

Note: `webtau` command line will exit with non zero code if there are `sscenario` tests present
 