Webtau tracks all actions and assertions tests perform. 
Actions and assertions are printed to console as well as available in the generated HTML report.

In a test details screen, there are multiple tabs.  
1. Layer specific details, e.g. HTTP, CLI, DB, Servers, etc
2. Every action that was performed

:include-image: doc-artifacts/reports/report-crud-http-calls.png {border: true, annotate: true, annotationsPath: "doc-artifacts/reports/report-crud-steps.json", fit: true}

:include-image: doc-artifacts/reports/report-crud-steps.png {border: true, fit: true}

Every webtau standard action generates `WebtauStep`. You can use webtau API to wrap your repeating actions 
into webtau compatible steps. 
