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
