# Read Eval Print Loop

Webtau command line tool comes with `REPL` mode that stands for 'read-eval-print-loop'. REPL is an established way to 
perform interactive execution of an API for the purpose of learning or experimenting. 
REPL preserves the context of execution and each new command you run can rely on the established context.

# Start REPL

To run webtau in REPL mode run

:include-cli-command: webtau repl 

:include-file: cli-tests/repl-context/out.txt {title: "REPL session"}

# Experiment With API

Use REPL to try out various apis like `http.`, `browser.`, `db.`, `cli.`, etc.

:include-file: cli-tests/http-repl-output/out.txt {title: "exploring http.get"}

# Setting Config Value

Use `cfg.url = "http://url"` to set base url for experiments.

:include-file: cli-tests/http-repl-cfg/out.txt {title: "setting cfg value"}

# Preserving Browser Context

In case of browser testing REPL preserves the context of an opened browser, so you can experiment
with css selectors and element actions without re-opening the browser and setting the right web app state.

:include-file: cli-tests/browser-repl-open/out.txt {title: "opened browser stays"}

Using element selectors provide additional information in REPL mode to help with exploration

:include-file: cli-tests/browser-repl-select/out.txt {title: "trying css selectors"}

