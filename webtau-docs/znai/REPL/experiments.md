# Read Eval Print Loop

Webtau command line tool comes with a `REPL` mode that stands for 'read-eval-print-loop'. REPL is an established way to 
perform interactive execution of an API for the purpose of learning or experimenting. 
The REPL preserves the context of execution and each new command you run can rely on the established context.

# Start REPL

To run webtau in REPL mode run

:include-cli-command: webtau repl 

:include-cli-output: cli-tests/repl-context/out.txt {title: "REPL session"}

# Experiment With API

Use the REPL to try out various apis like `http.`, `browser.`, `db.`, `cli.`, etc.

:include-cli-output: cli-tests/http-repl-output/out.txt {title: "exploring http.get"}

# Setting Config Value

Use `cfg.url = "http://url"` to set base url for experiments.

:include-cli-output: cli-tests/http-repl-cfg/out.txt {title: "setting cfg value"}

# Preserving Browser Context

During browser testing the REPL preserves the context of an opened browser, so you can experiment
with css selectors and element actions without re-opening the browser and setting the right web app state.

:include-cli-output: cli-tests/browser-repl-open/out.txt {title: "opened browser stays"}

Using element selectors provides additional information in REPL mode to help with exploration

:include-cli-output: cli-tests/browser-repl-select/out.txt {title: "trying css selectors"}
