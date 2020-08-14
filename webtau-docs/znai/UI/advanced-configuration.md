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

# Window Size

Browser window size can be set using `windowWidth` and `windowHeight`

:include-file: scenarios/ui/windowSizeCfg.groovy

# Documentation Artifacts

By default all generated documentation artifacts (e.g. screenshots) are created in the current directory.
To override

:include-file: scenarios/ui/docArtifactsCfg.groovy
