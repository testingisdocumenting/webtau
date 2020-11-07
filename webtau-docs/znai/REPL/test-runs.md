# Incremental Tests Development

:include-meta: {bulletListType: "Steps"}

You can combine experimentation in REPL with actual test development. Your feedback loop becomes even faster:
* Try API
* Add the line to test
* Re-run test with REPL
* Experiment within preserved context

Enumerate test files you want to REPL with and add `repl` to get into incremental tests development mode

:include-cli-command: webtau testscipts/* repl 

# Test Files Listing 

Use `ls` command to list available test files. 

:include-cli-output: cli-tests/repl-tests-listing/out.txt {title: "listing available test files"}

Note: Listing will happen automatically at the start

# Test File Selection

:include-cli-output: cli-tests/repl-test-file-selection-by-index/out.txt {title: "select test file by index"}

:include-cli-output: cli-tests/repl-test-file-back/out.txt {title: "get back to file selection"}

:include-cli-output: cli-tests/repl-test-file-selection-by-text/out.txt {title: "select test file by text"}

# Single Test Scenario Run

Selected test file has three scenarios

:include-file: testscripts/dataDownload.groovy {title: "testscripts/dataDownload.groovy"}

To run scenario by index

:include-cli-output: cli-tests/repl-test-scenario-ran-by-idx/out.txt {title: "run test scenario by index"}

To run scenario by partial name match

:include-cli-output: cli-tests/repl-test-scenario-ran-by-text/out.txt {title: "run test scenario by text"}

# Re-run Last Run

Use `r` to re-run previous run

:include-cli-output: cli-tests/repl-test-scenario-re-run/out.txt {title: "run test scenario by text"}

# Multiple Test Scenarios Run

Use comma separated list of indexes or partial text match to run more than one scenario 

:include-cli-output: cli-tests/repl-test-scenario-ran-by-multiple-indexes/out.txt {title: "run multiple scenario by indexes"}

:include-cli-output: cli-tests/repl-test-scenario-ran-by-multiple-text-reverse/out.txt {title: "run multiple scenarios by partial text matches"}

Note: Order of execution follows the order of specified indexes or text

Use `r from:to` to run a range of scenarios. `From`, `to` can be either index or partial text match.

:include-cli-output: cli-tests/repl-test-scenario-ran-by-mixed-range/out.txt {title: "run multiple scenarios by mixed range"}

:include-cli-output: cli-tests/repl-test-scenario-ran-by-mixed-range-reverse/out.txt {title: "run multiple scenarios by range in reverse"}

# Select Scenarios Without Run

:include-file: testscripts/dataDownload.groovy {title: "testscripts/dataDownload.groovy"}

Lets run scenarios by ranage again

:include-cli-output: cli-tests/repl-test-scenario-ran-by-mixed-range-reverse/out.txt {title: "run multiple scenarios by range in reverse"}

Use `s` to display last ran or selected scenarios. These scenarios will be ran with `r`

:include-cli-output: cli-tests/repl-test-scenario-selected/out.txt {title: "display selected scenarios"}

:include-cli-output: cli-tests/repl-test-scenario-select-by-indexes/out.txt {title: "select scenarios without run"}



