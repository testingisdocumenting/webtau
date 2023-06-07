Use `browser.alert` to manipulate browser alerts.

# Dismiss Alert

To dismiss alert use:

:include-file: scenarios/ui/alerts.groovy {
  surroundedBy: "dismiss-alert"
}

:include-cli-output: doc-artifacts/alerts.groovy-console-output.txt {
  title: "console output",
  surroundedBy: "dismiss alert",
  startLine: "dismissing",
  endLine: "."
}

# Accept Alert

To accept alert use:

:include-file: scenarios/ui/alerts.groovy {
  surroundedBy: "accept-alert"
}

:include-cli-output: doc-artifacts/alerts.groovy-console-output.txt {
  title: "console output",
  surroundedBy: "accept alert",
  startLine: "accepting",
  endLine: "."
}

# Alert Text

To wait on or validate alert text use:

:include-file: scenarios/ui/alerts.groovy {
  surroundedBy: "wait-alert-text"
}

:include-cli-output: doc-artifacts/alerts.groovy-console-output.txt {
  title: "console output",
  surroundedBy: "wait on alert text",
  startLine: "waiting",
  endLine: "accepting",
  excludeEnd: true
}


