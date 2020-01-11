# Specifying Test Meta

To attach custom meta data to a test use `attachTestMetaValue` from `WebTauGroovyDsl`
 
:include-file: scenarios/concept/metaDataRaw.groovy { title: "attach test metadata"}

# Meta Data Encapsulation

Encapsulate repeated meta data into a separate file. 
 
:include-file: scenarios/concept/metaDataMethodBased.groovy { title: "encapsulated metadata"}

:include-file: scenarios/concept/Support.groovy { title: "Support.groovy"}

# Report

Test meta data will be available in a generated report as well as in test listeners.

:include-image: doc-artifacts/reports/report-test-meta.png {annotationsPath: "doc-artifacts/reports/report-test-meta.json"}

1. If meta is present, it will be displayed at the test summary page