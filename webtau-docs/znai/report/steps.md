# Grouping Actions

Use `step` core method to wrap a set of actions into a group

:include-file: scenarios/concept/stepGroup.groovy {
  title: "step group",
  surroundedBy: "wrap-step"
}

:include-file: scenarios/concept/stepGroup.groovy {
  title: "core methods import",
  includeRegexp: "WebTauGroovyDsl"
}

# Tracing Values

Use `trace` core method to render key, values pairs to console and to web report.
From report point of view `trace` is an empty step.

:include-file: scenarios/concept/trace.groovy {
  title: "trace key values",
  surroundedBy: "trace-map"
}

:include-file: scenarios/concept/trace.groovy {
  title: "core methods import",
  includeRegexp: "WebTauGroovyDsl"
}

# Grouping With Trace

Pass key values to `step` to combine `step` and `trace

:include-file: scenarios/concept/stepGroup.groovy {
   title: "step group with key values",
   surroundedBy: "wrap-step-key-value"
}

:include-image: doc-artifacts/reports/report-step-key-value.png {border: true, fit: true}
