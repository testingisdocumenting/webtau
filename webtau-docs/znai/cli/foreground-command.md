Webtau can run commands in `foreground` and in `background`. When command is ran in `foreground` webtau will 
wait for the command to finish.

# Run

To run a command use `cli.run` with a single string parameter that includes a command and 
all its arguments:   

:include-file: doc-artifacts/snippets/foreground-cli/withoutValidation.groovy {
  title: "run command"
}

# Run With Validation

:include-file: doc-artifacts/snippets/foreground-cli/withOutputValidation.groovy {
  title: "output validation"
}

:include-file: doc-artifacts/snippets/foreground-cli/withErrorValidation.groovy {
  title: "error validation"
}

:include-file: doc-artifacts/snippets/foreground-cli/withExitCodeValidation.groovy {
  title: "exit code validation"
}

# Implicit Exit Code Validation

Webtau performs implicit exit code validation and adds `exitCode` equals zero when 
you don't specify explicit `exitCode` validation. 

:include-file: doc-artifacts/snippets/foreground-cli/withoutValidation.groovy {
  title: "implicit exit validation happens here"
}

Example above is equivalent to 

:include-file: doc-artifacts/snippets/foreground-cli/implicitExitCodeBehindScenes.groovy {
  title: "exit code check webtau performs if you don't write explicit validation"
}

# Run Result

Use the result of `cli.run` if you need to process the output of the command.

:include-file: doc-artifacts/snippets/foreground-cli/runResult.groovy {
  title: "using run result"
}

Warning: Perform validation inside validation block so webtau can track what was checked.

:include-file: doc-artifacts/snippets/foreground-cli/runResultExtractOutput.groovy {
  title: "extract from output by regexp",
  startLine: "example",
  endLine: "example",
  excludeStartEnd: true
}

:include-file: doc-artifacts/snippets/foreground-cli/runResultExtractError.groovy {
  title: "extract from error by regexp",
  startLine: "example",
  endLine: "example",
  excludeStartEnd: true
}

# Working Dir

Use `cli.workingDir` as a second parameter to `cli.run` to set a working dir:

:include-file: doc-artifacts/snippets/foreground-cli-cfg/workingDir.groovy {
  title: "set working dir"
}

# Environment Variables

Use `cli.env` as a second parameter to `cli.run` to set the environment variables:

:include-file: doc-artifacts/snippets/foreground-cli-cfg/envVar.groovy {
  title: "set environment variable"
}

# Chain Cli Run Config

Combine configs by using `cli.env(...).workingDir(...)` in any order to set both:

:include-file: doc-artifacts/snippets/foreground-cli-cfg/envVarAndWorkingDir.groovy {
  title: "set environment variable and working dir"
}

# Path

To specify `PATH` to use for CLI commands lookup use

:include-file: scenarios/cli/webtau-with-path.cfg.groovy {title: "webtau.config.groovy"}

# Timeout

`cli.run` command fails if it doesn't complete in 30 seconds. 

To override default timeout use `cliTimeout` config value: 
:include-file: scenarios/cli/webtau-cli-timeout.cfg.groovy {title: "webtau.config.groovy"}

To override timeout for a specific `cli.run` use

:include-file: scenarios/cli/cliTimeoutLocalOverride.groovy {title: "local override", includeRegexp: "cli.timeout"}

Note: Timeout value is specified in milliseconds 
