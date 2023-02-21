:include-markdown: import-ref.md

# Generate Warning

Use `warning` core method to mark something that needs to be looked at eventually.

```tabs
Groovy:
:include-file: scenarios/concept/warning.groovy {
  title: "warning signatures",
  surroundedBy: ["warning-label", "warning-map", "warning-vararg"]
}

Java:
:include-file:  com/example/tests/junit5/WarningJavaTest.java {
  title: "warning signatures",
  surroundedBy: ["warning-label", "warning-map", "warning-vararg"]
}
```

Let's take a look at the full test file example to see how warnings gets displayed

```tabs
Groovy:
:include-file: scenarios/concept/warning.groovy {
  autoTitle: true,
  startLine: "import",
  commentsType: "remove"
}

:include-cli-output: doc-artifacts/warning.groovy-console-output.txt {
  title: "console output",
  startLine: "warning label",
  endLine: "v4",
}

Java:
:include-file:  com/example/tests/junit5/WarningJavaTest.java {
  autoTitle: true,
  startLine: "import",
  commentsType: "remove"
}

:include-cli-output: doc-artifacts/com.example.tests.junit5.WarningJavaTest-console-output.txt {
  title: "console output",
  startLine: "warningLabel",
  endLine: "v4",
}
```

# Web Report

Web Report displays warnings in Steps tab for a specific test, but also displays them at the summary page

```tabs
Groovy:
:include-image: doc-artifacts/reports/report-warning-groovy.png {
  title: "report steps tab",
  border: true,
  fit: true
}

Java:
:include-image: doc-artifacts/reports/report-warning-java.png {
  title: "report steps tab",
  border: true,
  fit: true
}
```

Summary of warnings will be displayed at the end of the run in the console and present on the generated HTML report summary view.

```tabs
Groovy:
:include-cli-output: doc-artifacts/warning.groovy-console-output.txt {
  title: "console output warnings summary", 
  startLine: "warning(s) in tests"
}

:include-image: doc-artifacts/reports/report-summary-warning-collapsed-groovy.png {
  title: "warnings summary web report",
  border: true,
  fit: true
}

Java:
:include-cli-output: doc-artifacts/com.example.tests.junit5.WarningJavaTest-console-output.txt {
  title: "console output warnings summary", 
  startLine: "warning(s) in tests"
}

:include-image: doc-artifacts/reports/report-summary-warning-collapsed-java.png {
  title: "warnings summary web report",
  border: true,
  fit: true
}
```
