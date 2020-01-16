# Specifying Test Metadata

To attach custom meta data to a test use `metadata` from `WebTauGroovyDsl`
 
:include-file: scenarios/concept/metadataRaw.groovy { title: "attach test metadata" }

# Meta Data Encapsulation

You can encapsulate repeated metadata into a separate file. 
 
:include-file: scenarios/concept/metadataMethodBased.groovy { title: "encapsulated metadata" }

:include-file: scenarios/concept/Support.groovy { title: "Support.groovy"}

# Report

Test meta data will be available in a generated report as well as in test listeners.

:include-image: doc-artifacts/reports/report-test-metadata.png {annotationsPath: "doc-artifacts/reports/report-test-metadata.json" }

1. If metadata is present, it will be displayed at the test summary page