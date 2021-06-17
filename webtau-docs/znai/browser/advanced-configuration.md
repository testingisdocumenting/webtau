# Timeouts

Default timeout in milliseconds for `waitTo` and `waitToNot`

`````columns
left:
```
waitTimeout = 25000
```
right:
```
--waitTimeout=25000
```
`````

# Browser Size

Browser window size can be set using `browserWidth` and `browserHeight`

:include-file: scenarios/ui/windowSize.cfg.groovy

# Documentation Artifacts

By default all generated documentation artifacts (e.g. screenshots) are created in the current directory.
To override

:include-file: scenarios/ui/docArtifacts.cfg.groovy
