# Wait And Should

Use `waitTo` to wait on a condition, and `should` to assert right away.
All the code below you can freely swap between `should` and `waitTo`.

# Text Value

:include-file: scenarios/ui/pageElementValues.groovy {
  surroundedBy: "text-validation"
}

:include-cli-output: doc-artifacts/pageElementValues.groovy-console-output.txt {
  title: "console output",
  surroundedBy: "check page element value",
  startLine: "expecting"
}

# Attribute Value

:include-file: scenarios/ui/pageElementValues.groovy {
  surroundedBy: "attribute-validation"
}

:include-cli-output: doc-artifacts/pageElementValues.groovy-console-output.txt {
  title: "console output",
  surroundedBy: "check attribute class value",
  startLine: "expecting"
}
