:include-markdown: import-ref.md

# Grouping Actions

Use `step` core method to wrap a set of actions into a group

```tabs
Groovy:
:include-file: scenarios/concept/stepGroup.groovy {
  title: "step group",
  surroundedBy: "wrap-step"
}

Java:
:include-file:  com/example/tests/junit5/StepTraceJavaTest.java {
  title: "step group",
  surroundedBy: "wrap-step"
}
```

:include-cli-output: doc-artifacts/com.example.tests.junit5.StepTraceJavaTest-console-output.txt {
  title: "console output",
  startLine: "group of actions",
  endLine: "group of actions"
}

# Grouping With Key Value

Pass key values to `step` to combine `step` and `trace

```tabs
Groovy:
:include-file: scenarios/concept/stepGroup.groovy {
   title: "step group with key values",
   surroundedBy: "wrap-step-key-value"
}

Java:
:include-file:  com/example/tests/junit5/StepTraceJavaTest.java {
  title: "step group with key values",
  surroundedBy: "wrap-step-key-value"
}
```

:include-cli-output: doc-artifacts/com.example.tests.junit5.StepTraceJavaTest-console-output.txt {
  title: "console output",
  startLine: "important actions",
  endLine: "important actions"
}

:include-image: doc-artifacts/reports/report-step-key-value.png {border: true, fit: true, title: "web report"}

