# Skipping Tests on Condition

Use `onlyWhen` if you need to skip tests based on a condition.

:include-file: scenarios/concept/conditionalCustomRegistrationSkip.groovy {title: "Conditional tests skip"}

Tests will still appear as part of your report but will be marked as skipped.

# Skipping Tests Based on Env

Use `onlyForEnv` shortcut if you need to skip tests for certain environment.

:include-file: scenarios/concept/conditionalEnvRegistrationSkip.groovy {title: "Environment specific tests"}

# Custom Shortcuts

Consider creating your project specific shortcuts to avoid boilerplate. 
Here is an example of `onlyForEnv` shortcut definition.

:include-groovy: com/twosigma/webtau/WebTauGroovyDsl.groovy {entry: "onlyForEnv", title: "Custom shortcut"}