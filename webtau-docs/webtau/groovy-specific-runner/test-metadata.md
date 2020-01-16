# Specifying Test Metadata

To attach custom metadata to a test use `metadata` from `WebTauGroovyDsl`
 
:include-file: scenarios/concept/metadataRaw.groovy { title: "test metadata", commentsType: "inline" }

`metadata` can be called multiple times outside of `scenario`. It will be applied to all the scenarios below.

:include-file: scenarios/concept/metadataRawTopLevelOverrides.groovy { title: "multiple top-level metadata", commentsType: "inline" }

Unlike previous example, "team B" will be set for scenario "three" and "four".

# Metadata Encapsulation

You can encapsulate repeated metadata into a separate file. 
 
:include-file: scenarios/concept/metadataMethodBased.groovy { title: "encapsulated metadata" }

:include-file: scenarios/concept/Support.groovy { title: "Support.groovy"}

# Report

Test metadata will be available in a generated report as well as in test listeners.

:include-image: doc-artifacts/reports/report-test-metadata.png {annotationsPath: "doc-artifacts/reports/report-test-metadata.json" }

1. If metadata is present, it will be displayed at the test summary page