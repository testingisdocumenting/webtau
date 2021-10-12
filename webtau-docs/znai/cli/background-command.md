Webtau can run commands in `foreground` and in `background`. When command is ran in `background` webtau  
will continue execution, leaving command to run its course.

# Run In Background

To run a command in a background use `cli.runInBackground` with a single string parameter that includes a command and 
all its arguments:   

:include-file: doc-artifacts/snippets/background-cli/run.groovy {
  title: "run command in background"
}

# Stop Command 

Use `.stop` to explicitly terminate the running background command

:include-file: doc-artifacts/snippets/background-cli/runAndStop.groovy {
  title: "command termination"
}

# Interacting With Command

We are going to test a "hello world" script that asks you for a name and greet you:

:include-file: scripts/hello-world {title: "scripts/hello-world"}

Use `.send` to send input to a running command. Use `.output.waitTo` to wait for command to rich a certain state:
 
:include-file: doc-artifacts/snippets/background-send-cli/sendInput.groovy {
  title: "wait and send input"
}


You can use `<<` instead of `.send` for extra syntax sugar:

:include-file: doc-artifacts/snippets/background-send-cli/sendInputShiftLeft.groovy {
  title: "use << as .send"
}

# Wait Timeout

By default, webtau `waitTo` waits 5 seconds for a condition to become true.
This value can be changed either globally by using `:identifier: waitTimeout {validationPath: "org/testingisdocumenting/webtau/cfg/WebTauConfig.java"}` config value

```groovy {title: "webtau.cfg.groovy"}
waitTimeout = 20000
```

To override wait timeout locally, use

:include-file: scenarios/cli/outputWait.groovy {
  title: "local timeout",
  surroundedBy: "local-timeout"
}

# Working Dir

Use `cli.workingDir` as a second parameter to `cli.runInBackground` to set a working dir:

:include-file: doc-artifacts/snippets/background-cli-cfg/workingDir.groovy {
  title: "set working dir"
}

# Environment Variables

Use `cli.env` as a second parameter to `cli.runInBackground` to set the environment variables:

:include-file: scripts/hello-env-var {autoTitle: true}

:include-file: doc-artifacts/snippets/background-cli-cfg/envVar.groovy {
  title: "set environment variable"
}

# Common Environment Variables

Set `cliEnv` config value with environment values that needs to be passed to each `cli.runInBackground`:

:include-file: scenarios/cli/webtau-cli-env-vars.cfg.groovy {
  title: "webtau.cfg.groovy",
  surroundedBy: "default-my-var"
}

:include-file: doc-artifacts/snippets/common-env-vars/background.groovy {
  title: "use environment variable from config"
}

# Chain Cli Run Config

Combine configs by using `cli.env(...).workingDir(...)` in any order to set both:

:include-file: doc-artifacts/snippets/background-cli-cfg/envVarAndWorkingDir.groovy {
  title: "set environment variable and working dir"
}

# Path

To specify `PATH` to use for CLI commands lookup use

:include-file: scenarios/cli/webtau-with-path.cfg.groovy {title: "webtau.cfg.groovy"}

