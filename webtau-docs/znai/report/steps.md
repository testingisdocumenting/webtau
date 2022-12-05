# Imports

Code below assumes

:include-file: scenarios/concept/stepGroup.groovy {
  title: "core methods import",
  includeRegexp: "WebTauGroovyDsl"
}

# Grouping Actions

Use `step` core method to wrap a set of actions into a group

:include-file: scenarios/concept/stepGroup.groovy {
  title: "step group",
  surroundedBy: "wrap-step"
}

# Tracing Values

Use `trace` core method to render key, values pairs to console and to web report.
From report point of view `trace` is an empty step.

:include-file: scenarios/concept/trace.groovy {
  title: "trace key values",
  surroundedBy: "trace-map"
}

# Grouping With Trace

Pass key values to `step` to combine `step` and `trace

:include-file: scenarios/concept/stepGroup.groovy {
   title: "step group with key values",
   surroundedBy: "wrap-step-key-value"
}

:include-image: doc-artifacts/reports/report-step-key-value.png {border: true, fit: true}

# Warning

Use `warning` core method to mark something that needs to be looked at eventually.

:include-file: scenarios/concept/warning.groovy {
  title: "warning key values",
  surroundedBy: ["warning-map", "warning-vararg"]
}

:include-cli-output: doc-artifacts/warning.groovy-console-output.txt {
  title: "console output",
  startLine: "warning message with map",
  endLine: "v4"
}

:include-image: doc-artifacts/reports/report-warning.png {
  title: "report steps tab",
  border: true,
  fit: true
}

Summary of warnings will be displayed at the end of the run